package com.mcg.jwt.demo.domain.exception;

import javax.validation.Constraint;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=EmailAspect.class)
public @interface EmailCheckExists {
    String message() default "{com.mcg.jwt.demo.domain.exception.EmailCheckExists.message}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
