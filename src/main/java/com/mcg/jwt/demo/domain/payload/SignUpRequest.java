package com.mcg.jwt.demo.domain.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcg.jwt.demo.domain.exception.EmailCheckExists;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SignUpRequest {

    @NotNull
    @Email(message = "Not right type email")
    private String email;

    @NotBlank
    @Size(min = 6,max = 20)
    private String password;

    @JsonProperty("role")
    private String role;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_no")
    private String phoneNo;

    private String address;
}
