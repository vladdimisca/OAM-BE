package com.oam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    INTERNAL_SERVER_ERROR(1, "Internal server error. Oops, something went wrong!"),
    ALREADY_EXISTS(2, "{0} already exists!"),
    NOT_FOUND(3, "The {0} with id={1} was not found!"),
    FORBIDDEN(4, "This action is forbidden!"),
    PASSWORDS_NOT_MATCHING(5, "The provided password is invalid!"),
    EMAIL_NOT_ASSOCIATED(6, "There is no account associated with this email!"),
    NOT_ALLOWED_TO_CREATE_APARTMENTS(7, "Only the administrators of this association can create apartments!"),
    MUST_BE_ADMIN_ASSOCIATION_MEMBER(8, "You must be an association member with admin rights to perform this action!");


    private final int errorCode;
    private final String errorMessage;
}
