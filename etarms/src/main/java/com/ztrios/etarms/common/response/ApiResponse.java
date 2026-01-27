package com.ztrios.etarms.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private final int statusCode;
    private final String message;
    private final T data;

    private ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(int statusCode, String message, T data) {
        return new ApiResponse<>(statusCode, message, data);
    }

//    public static <T> ApiResponse<T> error(int statusCode, String message, T data) {
//        return new ApiResponse<>(statusCode, message, data);
//    }

//    public int getStatusCode() {
//        return statusCode;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public T getData() {
//        return data;
//    }
}

