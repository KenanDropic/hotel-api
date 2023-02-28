package com.hotel.utils.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class ResponsePayload<T> {
    private boolean success;
    private String message;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> token;
    private T data;

    public ResponsePayload(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = Optional.ofNullable(token);
    }

    public ResponsePayload(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponsePayload(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResponsePayload(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
