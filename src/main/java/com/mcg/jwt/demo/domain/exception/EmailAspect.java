package com.mcg.jwt.demo.domain.exception;

import com.mcg.jwt.demo.domain.repository.UserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Aspect
@Component
public class EmailAspect implements ConstraintValidator<EmailCheckExists, String> {
    @Autowired private UserRepository userRepository;

    private boolean existsEmail(String email){
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public void initialize(EmailCheckExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (existsEmail(s)){
            return false;
        }else return true;
    }
}
