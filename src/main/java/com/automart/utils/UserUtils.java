package com.automart.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class UserUtils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHANUMERIC ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    public String generateCarId(int length){
        return generateRandomString(length);
    }

    public String generateAdressId(int length){
        return generateRandomString(length);
    }

    public String generateOrderId(int length){
        return generateRandomString(length);
    }

    public String generateSubscriptionId(int length){
        return generateRandomString(length);
    }
    public String generateTransactionId(int length){
        return generateRandomString(length);
    }
    private String generateRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return new String(returnValue);
    }
}