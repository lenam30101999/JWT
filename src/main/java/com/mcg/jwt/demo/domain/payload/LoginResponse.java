package com.mcg.jwt.demo.domain.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("type")
    private String type = "Bearer";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
