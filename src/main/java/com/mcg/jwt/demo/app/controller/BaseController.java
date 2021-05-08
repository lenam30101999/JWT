package com.mcg.jwt.demo.app.controller;

import com.mcg.jwt.demo.domain.payload.LoginRequest;
import com.mcg.jwt.demo.domain.payload.RefreshTokenRequest;
import com.mcg.jwt.demo.domain.payload.SignUpRequest;
import com.mcg.jwt.demo.domain.service.ProfileService;
import com.mcg.jwt.demo.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class BaseController {

    @Autowired
    protected UserService userService;

    @Autowired
    protected ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokenUser(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.genNewAccessToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.logoutRequest(refreshTokenRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.save(signUpRequest);
    }

}
