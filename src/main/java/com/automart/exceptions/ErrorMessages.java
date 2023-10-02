package com.automart.exceptions;

import lombok.*;

@Getter

@AllArgsConstructor
public enum ErrorMessages {
    RECORD_NOT_FOUND("User not found"),
    ONLY_SELLERS_CAN_UPDATE_STATUS("Only Sellers can Accept or Reject Order"),

    ONLY_BUYERS_CAN_UPDATE_ORDER_PRICE("Only Buyers can Update Order Price"),
    RECORD_ALREADY_EXISTS("User already exists"),
    DETAILS_ALREADY_EXISTS("Please Provide a Different User Detail"),
   ALREADY_SUBSCRIBED(" You are already subscribed to this user"),
    INSUFFICIENT_FUNDS("Insufficient funds"),
    INVALID_VALUE("Please enter a valid amount");
    private String errorMessage;

    public void setErrorMessage(String errorMessage) {

        this.errorMessage = errorMessage;
    }
}
