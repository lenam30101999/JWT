package com.mcg.jwt.demo.app.controller;

import com.mcg.jwt.demo.app.dto.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    @GetMapping("/message")
    public Message message(){
        return new Message("JWT Hợp lệ");
    }

    @PutMapping("/active")
    public ResponseEntity<?> urlActive(@RequestParam("email") String email){
        return userService.changeState(email);
    }

}
