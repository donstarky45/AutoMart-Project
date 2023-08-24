package com.SecurityAug13th.RealTutorial.exceptions;

import lombok.*;

@Getter

@AllArgsConstructor
public enum ErrorMessages {
    RECORD_NOT_FOUND("Record not found"),
    RECORD_ALREADY_EXISTS("User already exists"),
    INSUFFICIENT_FUNDS("Insufficient funds"),
    INVALID_VALUE("Please enter a valid amount");
    private String errorMessage;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
