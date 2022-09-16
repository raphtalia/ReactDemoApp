package reactdemoapp.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactdemoapp.backend.models.RefreshToken;
import reactdemoapp.backend.models.User;
import reactdemoapp.backend.payload.request.SignInRequest;
import reactdemoapp.backend.payload.request.SignUpRequest;
import reactdemoapp.backend.payload.response.JwtResponse;
import reactdemoapp.backend.payload.response.UserResponse;
import reactdemoapp.backend.services.TokenService;
import reactdemoapp.backend.services.UserService;
import reactdemoapp.backend.utils.jwt.AccessToken;
import reactdemoapp.backend.utils.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @PostMapping("signUp")
    public ResponseEntity<JwtResponse> signUp(@RequestBody SignUpRequest body){
        try {
            User user = userService.save(body.toModel());
            AccessToken accessToken = jwtUtils.generateAccessToken(user);
            RefreshToken refreshToken = tokenService.generateRefreshToken(user, accessToken.getToken());

            return ResponseEntity.ok(new JwtResponse()
                    .setEmail(user.getEmail())
                    .setAccess_token(accessToken.getToken())
                    .setAccess_token_time_created(accessToken.getTimeCreated())
                    .setAccess_token_expires_in(accessToken.getExpiresIn())
                    .setRefresh_token(refreshToken.getToken())
                    .setRefresh_token_time_created(refreshToken.getTimeCreated().toInstant())
                    .setRefresh_token_expires_in(refreshToken.getExpiresIn()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("signIn")
    public ResponseEntity<JwtResponse> signIn(@RequestBody SignInRequest body) {
        try {
            User user = userService.findByEmailAndPassword(body.getEmail(), body.getPassword());

            if (user == null) {
                return null;
            }

            AccessToken accessToken = jwtUtils.generateAccessToken(user);
            RefreshToken refreshToken = tokenService.generateRefreshToken(user, accessToken.getToken());
            return ResponseEntity.ok(new JwtResponse()
                    .setEmail(user.getEmail())
                    .setAccess_token(accessToken.getToken())
                    .setAccess_token_time_created(accessToken.getTimeCreated())
                    .setAccess_token_expires_in(accessToken.getExpiresIn())
                    .setRefresh_token(refreshToken.getToken())
                    .setRefresh_token_time_created(refreshToken.getTimeCreated().toInstant())
                    .setRefresh_token_expires_in(refreshToken.getExpiresIn()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint for using a refresh token to get a new JWT
    @PostMapping("token")
    public ResponseEntity<JwtResponse> useToken(@RequestParam("token") String token) {
        try {
            AccessToken accessToken = tokenService.useRefreshToken(token);
            User user = userService.findByEmail(accessToken.getEmail());
            RefreshToken refreshToken = tokenService.generateRefreshToken(user, accessToken.getToken());
            return ResponseEntity.ok(new JwtResponse()
                    .setEmail(user.getEmail())
                    .setAccess_token(accessToken.getToken())
                    .setAccess_token_time_created(accessToken.getTimeCreated())
                    .setAccess_token_expires_in(accessToken.getExpiresIn())
                    .setRefresh_token(refreshToken.getToken())
                    .setRefresh_token_time_created(refreshToken.getTimeCreated().toInstant())
                    .setRefresh_token_expires_in(refreshToken.getExpiresIn()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
