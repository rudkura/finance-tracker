package com.example.FinanceTrackerBackend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCode,
        String message,
        Map<String, String> fieldErrors
) {
    // factory w/o error fields
    public static ErrorResponse of(String errCode, String msg) {
        return new ErrorResponse(errCode, msg, null);
    }

    // factory w/ fields
    public static ErrorResponse of(String errCode, String msg, Map<String, String> fieldErrors) {
        return new ErrorResponse(errCode, msg, fieldErrors);
    }
}
