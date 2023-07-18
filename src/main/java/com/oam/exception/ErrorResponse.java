package com.oam.exception;

import java.util.List;

public record ErrorResponse(List<ErrorEntity> errorMessages) {
}
