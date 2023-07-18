package com.oam.exception.model;

import com.oam.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractApiException {
    public NotFoundException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.NOT_FOUND, errorMessage, params);
    }
}
