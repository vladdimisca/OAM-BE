package com.oam.exception.model;

import com.oam.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractApiException {
    public ForbiddenException(ErrorMessage errorMessage, Object... params) {
        super(HttpStatus.FORBIDDEN, errorMessage, params);
    }
}
