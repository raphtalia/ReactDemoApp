package reactdemoapp.backend.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.filter.GenericFilterBean;
import reactdemoapp.backend.models.BlockedJwt;
import reactdemoapp.backend.models.User;
import reactdemoapp.backend.repositories.BlockedJwtRepository;
import reactdemoapp.backend.repositories.RefreshTokenRepository;
import reactdemoapp.backend.repositories.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class RestConfig {
    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    BlockedJwtRepository blockedJwtRepository;

    @Bean
    public GenericFilterBean jwtFilter() {
        return new GenericFilterBean() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse res = (HttpServletResponse) response;
                String token = req.getHeader("Authorization");

                // Prevents from running on permitAll endpoints
                if (token != null) {
                    try {
                        // Block tokens that have been signed out or "redeemed" through a refresh token
                        if (blockedJwtRepository.findByToken(token.replace("Bearer ", "")) != null) {
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            return;
                        }
                    } catch (Exception e) {
                        res.setStatus(HttpStatus.BAD_REQUEST.value());
                        return;
                    }
                }
                chain.doFilter(req, res);
            }
        };
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            // TODO: Fix HttpStatus codes not returning in response
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            String token = request.getHeader("Authorization");
            if (token == null) {
                return;
            }

            token = token.replace("Bearer ", "");
            Jwt jwt = jwtDecoder().decode(token);
            String email = jwt.getSubject();
            Timestamp issuedAt = new Timestamp(jwt.getIssuedAt().toEpochMilli());
            int expiresIn = (int) (jwt.getExpiresAt().toEpochMilli() - jwt.getIssuedAt().toEpochMilli()) / 1000;

            User user = userRepository.findByEmail(email);
            if (user == null) {
                return;
            }

            // TODO: Implement services somehow
            // Invalidate the JWT now that the user has signed out
            blockedJwtRepository.save(new BlockedJwt()
                    .setToken(token)
                    .setUserId(user.getUserId())
                    .setTimeCreated(issuedAt)
                    .setExpiresIn(expiresIn));
            // RefreshTokenService
            // Remove the used refresh token
            refreshTokenRepository.findByUserId(user.getUserId()).forEach(refreshToken -> refreshTokenRepository.delete(refreshToken));

            response.setStatus(HttpStatus.OK.value());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        // Allow unauthorized requests to endpoints used to obtain a JWT
                        .antMatchers("/api/v1/users/signIn", "/api/v1/users/signUp", "/api/v1/users/token").permitAll()
                        // Require authorization for all other endpoints
                        .antMatchers("/api/v1/users/*").authenticated()
                )
                .addFilterAfter(jwtFilter(), AuthorizationFilter.class) // TODO: Figure out filter order
                .csrf()
                .disable()
                .httpBasic(Customizer.withDefaults())
                .logout()
                .logoutUrl("/api/v1/users/signOut")
                .addLogoutHandler(logoutHandler())
                .invalidateHttpSession(true)
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        return users;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}
