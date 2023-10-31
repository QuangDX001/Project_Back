package com.example.backend.security.config;

import java.util.Random;

/**
 * Created by Admin on 10/27/2023
 */
public class StringUtil {
    public String randomString() {
        int length = 6;
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#&()–[{}]:;',?/*~$^+=<>";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        boolean check = false;
        while (check == false) {
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(allowedChars.length());
                char randomChar = allowedChars.charAt(index);
                sb.append(randomChar);
            }
            if (sb.toString().matches(AppConstants.PASSWORD_REGEX)) {
                check = true;
            } else {
                check = false;
                sb.setLength(0);
            }
        }
        return sb.toString();
    }
}
