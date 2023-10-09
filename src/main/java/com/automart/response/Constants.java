package com.automart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor
public enum Constants {
    AVAILABLE("AVAILABLE"),
    SOLD("SOLD"),
    RECORD_ALREADY_EXISTS("User already exists"),
    DETAILS_ALREADY_EXISTS("Please Provide a Different User Detail"),
   NEW("new"),
    USED("used"),
    INSUFFICIENT_FUNDS("Insufficient funds"),
    INVALID_VALUE("Please enter a valid amount");
    private String Message;

    public void setMessage(String errorMessage) {
        this.Message = Message;
    }
}
