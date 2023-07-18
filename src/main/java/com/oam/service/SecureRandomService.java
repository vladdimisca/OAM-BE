package com.oam.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SecureRandomService {

    private final SecureRandom random = new SecureRandom();

    public String generateRandomPassword() {
        int passwordLength = 8;
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}
