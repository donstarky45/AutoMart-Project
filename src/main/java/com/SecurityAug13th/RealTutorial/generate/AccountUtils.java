package com.SecurityAug13th.RealTutorial.generate;

import java.security.SecureRandom;
import java.util.Random;

public class AccountUtils {
    private final Random RANDOM = new SecureRandom();
    private final String NUMBERS ="0123456789";
    public final String INITIALS = "209";

    public String generate(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);
        returnValue.append(INITIALS);
        for (int i = 0; i < length-3; i++) {
            returnValue.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return new String(returnValue);
    }
}