package com.oam.exception.model;

import com.oam.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractApiException {
    public BadRequestException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.BAD_REQUEST, errorMessage, params);
    }
}
