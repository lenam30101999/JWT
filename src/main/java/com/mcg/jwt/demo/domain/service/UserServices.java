package com.mcg.jwt.demo.domain.service;

import com.mcg.jwt.demo.domain.constants.Constants;
import com.mcg.jwt.demo.domain.payload.ApiResponse;
import com.mcg.jwt.demo.domain.payload.LoginRequest;
import com.mcg.jwt.demo.domain.payload.LoginResponse;
import com.mcg.jwt.demo.domain.payload.SignUpRequest;
import com.mcg.jwt.demo.domain.entity.CustomUserDetails;
import com.mcg.jwt.demo.domain.entity.types.Role;
import com.mcg.jwt.demo.domain.entity.types.State;
import com.mcg.jwt.demo.domain.entity.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Log4j2
@Service
@Transactional
public class UserServices extends BaseServices implements UserDetailsService {

    public ResponseEntity<?> save(SignUpRequest signUpRequest){
        if (!EmailValidator.getInstance().isValid(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false, "Email is wrong type!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (existsEmail(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false, "Email already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .state(State.NONACTIVE)
                .build();
        userRepository.save(user);
        this.sendEmail(user.getEmail());
        return ResponseEntity.ok(
                new ApiResponse(true, "User registered successfully! Check your email to active account"));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user.getState().equals(State.NONACTIVE)){
            return new ResponseEntity(new ApiResponse(false, "Active your account first!"),
                    HttpStatus.BAD_REQUEST);
        }else{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new LoginResponse(jwt));
        }
    }

    public ResponseEntity<?> changeState(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if (Objects.nonNull(user)){
            user.setState(State.ACTIVE);
            userRepository.saveAndFlush(user);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Success!"));
        }else {
            return new ResponseEntity(new ApiResponse(false, "Failed!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private boolean existsEmail(String email){
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                new UsernameNotFoundException("User not found with email : " + email)
        );
        return new CustomUserDetails(user);
    }

    private void sendEmail(String email) {
        String url = Constants.url + email;
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Email active account!");
        message.setText("Click url below to active your account:" + url);

        this.emailSender.send(message);
        log.info("Email Sent!");
    }

    public UserDetails loadUserById(int id){
        User user = userRepository.findById(id);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Not found");
        }
        return new CustomUserDetails(user);
    }
}
