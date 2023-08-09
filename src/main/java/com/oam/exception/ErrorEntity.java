package com.oam.exception;

public record ErrorEntity(int errorCode, String errorMessage, String fieldName) {
    public ErrorEntity(int errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }
}
