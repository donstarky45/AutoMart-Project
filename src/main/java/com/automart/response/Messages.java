package com.automart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor
public enum Messages {
    POSTED("ADs Posted Successfully"),
    RECORD_ALREADY_EXISTS("User already exists"),
    DETAILS_ALREADY_EXISTS("Please Provide a Different User Detail"),
   ALREADY_SUBSCRIBED(" You are already subscribed to this user"),
    INSUFFICIENT_FUNDS("Insufficient funds"),
    INVALID_VALUE("Please enter a valid amount");
    private String Message;

    public void setMessage(String errorMessage) {
        this.Message = Message;
    }
}
