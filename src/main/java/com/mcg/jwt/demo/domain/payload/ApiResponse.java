package com.mcg.jwt.demo.domain.payload;

import lombok.Data;

import java.time.Clock;
import java.time.Instant;

@Data
public class ApiResponse {

    private String message;

    private Instant timestamp;

    public ApiResponse(String message) {
        Clock clock = Clock.systemDefaultZone();
        this.message = message;
        this.timestamp = clock.instant();
    }
}
