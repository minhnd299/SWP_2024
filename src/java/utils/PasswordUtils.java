/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.SecureRandom;

/**
 *
 * @author ADMIN
 */
public class PasswordUtils {

    private static final String UPPERCASECHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASECHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBBER = "0123456789";
    private static final String SPECIALCHAR = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generateRandomPassword() {

        String allChars = UPPERCASECHAR + LOWERCASECHAR + NUMBBER + SPECIALCHAR;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
    }
}
