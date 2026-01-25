package com.ztrios.etarms.common.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
