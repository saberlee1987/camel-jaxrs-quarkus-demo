package com.example.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidatorException extends RuntimeException {
    private String fieldName;
    private String detailMessage;

    public ValidatorException(String fieldName, String detailMessage) {
        this.fieldName = fieldName;
        this.detailMessage = detailMessage;
    }

    public ValidatorException(String message, String fieldName, String detailMessage) {
        super(message);
        this.fieldName = fieldName;
        this.detailMessage = detailMessage;
    }

    public ValidatorException(String message, Throwable cause, String fieldName, String detailMessage) {
        super(message, cause);
        this.fieldName = fieldName;
        this.detailMessage = detailMessage;
    }

    public ValidatorException(Throwable cause, String fieldName, String detailMessage) {
        super(cause);
        this.fieldName = fieldName;
        this.detailMessage = detailMessage;
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String fieldName, String detailMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.fieldName = fieldName;
        this.detailMessage = detailMessage;
    }

}
