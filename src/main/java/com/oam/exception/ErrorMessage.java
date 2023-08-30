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
    MUST_BE_ADMIN_ASSOCIATION_MEMBER(8, "You must be an association member with admin rights to perform this action!"),
    INVALID_ASSOCIATION_CODE(9, "The provided association code is invalid!"),
    PRICE_PER_INDEX_UNIT_NOT_PROVIDED(10, "Price per index unit must be provided for invoices distributed per counter!"),
    INVOICE_NUMBER_NOT_EXISTING(11, "There is no existing invoice associated with the provided number!"),
    CANNOT_DELETE_PAID_INVOICES(12, "You can not delete a invoice which was paid by some members!"),
    CANNOT_DELETE_INDEX(13, "You can not delete an index which has a corresponding invoice!"),
    NEW_INDEX_MUST_BE_GREATER_THAN_OLD_INDEX(14, "The value of the new index must be greater than the one of the old index!"),
    INDEX_ALREADY_UPLOADED(15, "The index was already uploaded!"),
    EMPTY_INVOICE_DISTRIBUTION_LIST(16, "You must select at least one invoice to pay!"),
    PAYMENT_ALREADY_DONE(17, "There is a payment which is already in progress or finished!"),
    MUST_BE_ASSOCIATION_MEMBER(18, "You must be an association member to perform this action!"),
    CANNOT_REMOVE_LAST_ADMIN(19, "You cannot remove the last admin from an association!"),
    ACCOUNT_IS_BANNED(20, "This account has been banned by the administrators!");


    private final int errorCode;
    private final String errorMessage;
}
