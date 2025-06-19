package com.example.FinanceTrackerBackend.exception.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Category
    CATEGORY_DUPLICATE(HttpStatus.BAD_REQUEST, "Category name already exists for user."), //400
    CATEGORY_HAS_TRANSACTIONS(HttpStatus.BAD_REQUEST, "Categories that have transactions cannot be deleted. First, migrate all transactions to a different category"), //400
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested category not found."), //404
    CATEGORY_TYPE_UPDATE_FORBIDDEN(HttpStatus.BAD_REQUEST, "Category type cannot be changed once created. You can create a category of the same name with different type"), //400

    // Transaction
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested transaction not found."), //404

    // Auth
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid login credentials."), //401
    UNAUTHORISED(HttpStatus.UNAUTHORIZED, "You are not authorised."), //401
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "User with this email address already exists."), //409
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "You do not have permission for this action."), //403
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or missing token"), //401

    // Common
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed."), //400
    INVALID_JSON(HttpStatus.BAD_REQUEST, "Invalid data format."), //400
    MISSING_PARAM(HttpStatus.BAD_REQUEST, "Required parameter is missing."), //400
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "Query parameters invalid."), //400
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource not found."), //404
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "Invalid request parameters."), //400
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "Date from must be before date to"), //400

    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.") //500
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
