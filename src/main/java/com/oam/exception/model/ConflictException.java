package com.oam.exception.model;

import com.oam.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ConflictException extends AbstractApiException {
    public ConflictException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.CONFLICT, errorMessage, params);
    }
}
