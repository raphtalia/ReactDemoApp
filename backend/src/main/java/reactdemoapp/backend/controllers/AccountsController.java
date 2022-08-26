package reactdemoapp.backend.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactdemoapp.backend.models.AccessToken;
import reactdemoapp.backend.models.RefreshToken;
import reactdemoapp.backend.payload.response.JwtResponse;
import reactdemoapp.backend.security.jwt.JwtUtils;
import reactdemoapp.backend.services.RefreshTokenService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class AccountsController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

//    @PostMapping("/signUp")
//    public String signUp() {}

    @PostMapping("/signIn")
    public JwtResponse signIn(Authentication auth) {
        AccessToken accessToken = jwtUtils.generateAccessToken(auth);
        return new JwtResponse(
                accessToken.getUsername(),
                accessToken.getToken(),
                refreshTokenService.createRefreshToken(auth.getName()).getToken(),
                accessToken.getExpiresIn()
        );
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> token(@RequestParam("token") String token) {
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(token).get();
            refreshTokenService.invalidateRefreshToken(refreshToken);
            AccessToken accessToken = jwtUtils.generateAccessTokenFromUsername(refreshToken.getUsername());
            return new ResponseEntity<>(new JwtResponse(
                    accessToken.getUsername(),
                    accessToken.getToken(),
                    refreshTokenService.createRefreshToken(refreshToken.getUsername()).getToken(),
                    accessToken.getExpiresIn()
            ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping("/update")
//    public String update() {}
}
