package com.mcg.jwt.demo.domain.service;

import com.mcg.jwt.demo.domain.jwt.JwtTokenProvider;
import com.mcg.jwt.demo.domain.mapper.ModelMapper;
import com.mcg.jwt.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BaseServices {
    @Autowired protected UserRepository userRepository;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected AuthenticationManager authenticationManager;
    @Autowired protected JwtTokenProvider tokenProvider;
    @Autowired protected JavaMailSender emailSender;
    @Autowired protected ModelMapper modelMapper;
}
