package com.ztrios.etarms.common.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ErrorResponse of(     //static factory method which internally calls constructor
            int status,
            String error,
            String message,
            String path
    ) {
        return new ErrorResponse(
                Instant.now(),
                status,
                error,
                message,
                path
        );
    }
}
