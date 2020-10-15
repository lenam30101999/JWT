package com.mcg.jwt.demo.domain.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private HttpStatus status;

    @JsonProperty("code")
    private int statusCode;

    private String message;

    private Instant timestamp;

    private boolean isSuccess;

    public ApiResponse(String message, boolean isSuccess) {
        Clock clock = Clock.systemDefaultZone();
        this.status = HttpStatus.OK;
        this.statusCode = status.value();
        this.message = message;
        this.isSuccess = isSuccess;
        this.timestamp = clock.instant();
    }

    public ApiResponse(HttpStatus status, String message, boolean isSuccess) {
        Clock clock = Clock.systemDefaultZone();
        this.timestamp = clock.instant();
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
