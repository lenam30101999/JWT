package com.mcg.jwt.demo.domain.service;

import com.mcg.jwt.demo.domain.constants.Constants;
import com.mcg.jwt.demo.domain.entity.Profile;
import com.mcg.jwt.demo.domain.payload.*;
import com.mcg.jwt.demo.domain.entity.CustomUserDetails;
import com.mcg.jwt.demo.domain.entity.types.Role;
import com.mcg.jwt.demo.domain.entity.types.State;
import com.mcg.jwt.demo.domain.entity.User;
import com.mcg.jwt.demo.domain.utils.GenKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@Transactional
public class UserService extends BaseService implements UserDetailsService {

    public ResponseEntity<?> save(SignUpRequest signUpRequest){
        if (existsEmail(signUpRequest.getEmail())){
            return utils.createErrorResponse(
                    new ApiResponse(HttpStatus.BAD_REQUEST, "Email have exists!", false));
        }

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .state(State.ACTIVE)
                .build();
        Profile profile = Profile.builder()
                .fullName(signUpRequest.getFullName())
                .phoneNo(signUpRequest.getPhoneNo())
                .address(signUpRequest.getAddress())
                .user(user)
                .build();

        user.setProfile(profile);
        userRepository.save(user);
        this.sendEmail(signUpRequest.getEmail());
        return utils.createOkResponse(
                new ApiResponse("User registered successfully! Check your email to active account", true));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (Objects.isNull(user)){
            return utils.createErrorResponse(
                new ApiResponse(HttpStatus.BAD_REQUEST, "Your account is not exists!", false));
        }

        if (user.getState().equals(State.NONACTIVE)){
            return utils.createErrorResponse(
                    new ApiResponse(HttpStatus.BAD_REQUEST, "Active your account first!", false));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateAccessToken((CustomUserDetails) authentication.getPrincipal());
        String refreshToken = tokenProvider.generateRefreshToken((CustomUserDetails) authentication.getPrincipal());
        return utils.createOkResponse(new LoginResponse(jwt, refreshToken));
    }

    public ResponseEntity<?> genNewAccessToken(RefreshTokenRequest refreshTokenRequest){
        String key = refreshTokenRequest.getRefreshToken();
        String refreshTokenFromCache = cacheManager.get(GenKey.genRefreshKey(key));
        if (Objects.nonNull(refreshTokenFromCache)){
            int id = tokenProvider.getIdFromSubjectJWT(key);
            UserDetails user = loadUserById(id);
            String jwt = tokenProvider.generateAccessToken((CustomUserDetails) user);
            return utils.createOkResponse(new LoginResponse(jwt, null));
        }else return utils.createErrorResponse(
                new ApiResponse(HttpStatus.BAD_REQUEST, "You have been logout!", false));
    }

    public ResponseEntity<?> logoutRequest(RefreshTokenRequest refreshTokenRequest){
        cacheManager.del(GenKey.genRefreshKey(refreshTokenRequest.getRefreshToken()));
        return utils.createErrorResponse(
                new ApiResponse(HttpStatus.BAD_REQUEST, "Logout!", false));
    }

    public ResponseEntity<?> changeState(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if (Objects.nonNull(user)){
            user.setState(State.ACTIVE);
            userRepository.saveAndFlush(user);
            return utils.createOkResponse(new ApiResponse("Success!", true));
        }else {
            return utils.createErrorResponse(
                    new ApiResponse(HttpStatus.BAD_REQUEST, "Active failed!", false));
        }
    }

    public ResponseEntity<?> message(){
        return utils.createOkResponse("JWT hợp lệ!");
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        CustomUserDetails customUserDetails = new CustomUserDetails(user, authorities);
        return customUserDetails;
    }

//    public ResponseEntity<?> sendMessageTo(String email){
//        this.sendEmail(email);
//        return ResponseEntity.ok("OK");
//    }

    private void sendEmail(String email) {
        String url = Constants.url + email;
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Email active account");
        message.setText("Click url below to active your account:" + url);

        this.emailSender.send(message);
        log.info("Email Sent!");
    }

    public UserDetails loadUserById(int id){
        User user = userRepository.findById(id);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Not found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        CustomUserDetails customUserDetails = new CustomUserDetails(user, authorities);
        return customUserDetails;
    }

    private boolean existsEmail(String email){
        return userRepository.existsUserByEmail(email);
    }
}

