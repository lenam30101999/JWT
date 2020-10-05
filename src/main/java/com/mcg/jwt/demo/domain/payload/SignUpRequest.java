package com.mcg.jwt.demo.domain.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcg.jwt.demo.app.dto.ProfileDTO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6,max = 20)
    private String password;

    @JsonProperty("profile")
    private ProfileDTO profileDTO;
}
