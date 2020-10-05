package com.mcg.jwt.demo.app.controller;

import com.mcg.jwt.demo.domain.payload.LoginRequest;
import com.mcg.jwt.demo.domain.payload.SignUpRequest;
import com.mcg.jwt.demo.domain.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class BaseController {

    @Autowired
    protected UserServices userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.save(signUpRequest);
    }

}
