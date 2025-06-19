package com.example.FinanceTrackerBackend.exception;

import com.example.FinanceTrackerBackend.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode code;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode;
    }

}
