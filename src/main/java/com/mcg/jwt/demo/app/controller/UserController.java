package com.mcg.jwt.demo.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    @GetMapping("/message")
    public ResponseEntity<?> message(){
        return userService.message();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfileById(@RequestHeader("Authorization") String header){
        return profileService.getProfileById(header);
    }

    @PutMapping("/active")
    public ResponseEntity<?> urlActive(@RequestParam("email") String email){
        return userService.changeState(email);
    }

//    @PostMapping("/email")
//    public ResponseEntity<?> sendEmail(){
//        String email = "lon3w0lfvn2@gmail.com";
//        return userService.sendMessageTo(email);
//    }
}
