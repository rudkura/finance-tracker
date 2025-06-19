package com.example.FinanceTrackerBackend.controller.advice;

import com.example.FinanceTrackerBackend.exception.ApiException;
import com.example.FinanceTrackerBackend.exception.constant.ErrorCode;
import com.example.FinanceTrackerBackend.model.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        return getResponse(ex.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return getResponse(ErrorCode.VALIDATION_ERROR, ex.getBindingResult());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleCredentialsException(BadCredentialsException ex) {
        return getResponse(ErrorCode.BAD_CREDENTIALS);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleParse(HttpMessageNotReadableException ex) {
        return getResponse(ErrorCode.INVALID_JSON);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return getResponse(ErrorCode.MISSING_PARAM);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex) {
        return getResponse(ErrorCode.CONSTRAINT_VIOLATION);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return getResponse(ErrorCode.ACCESS_DENIED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return getResponse(ErrorCode.ENTITY_NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return getResponse(ErrorCode.INVALID_ARGUMENT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        return getResponse(ErrorCode.UNEXPECTED_ERROR);
    }

    // region: private methods

    private ResponseEntity<ErrorResponse> getResponse(ErrorCode code) {
        return ResponseEntity.status(code.getStatus())
                .body(ErrorResponse.of(code.name(), code.getMessage()));
    }

    private ResponseEntity<ErrorResponse> getResponse(ErrorCode code, BindingResult bindingResult) {
        Map<String, String> fieldErrs = new HashMap<>();
        bindingResult.getFieldErrors()
                .forEach(fe -> fieldErrs.put(fe.getField(), fe.getDefaultMessage()));

        return ResponseEntity.status(code.getStatus())
                .body(ErrorResponse.of(code.name(), code.getMessage(), fieldErrs));
    }

    // endregion

}
