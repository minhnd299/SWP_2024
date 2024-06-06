
import java.util.Random;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class TestSome {

    public static void main(String[] args) {
        String password = generateStrongPassword();
        System.out.println("Generated password: " + password);

        // Regex to validate the password
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-_])(?=\\S+$).{8}$";
        boolean isValid = password.matches(regex);
        System.out.println("Password is valid: " + isValid);
    }

    private static String generateStrongPassword() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=-_";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        String categories = "[0-9]";
        boolean hasDigit = false;
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasSpecial = false;

        while (password.length() < length) {
            char character = characters.charAt(random.nextInt(characters.length()));
            password.append(character);

            // Check for character categories
            if (Character.isDigit(character)) {
                hasDigit = true;
            } else if (Character.isLowerCase(character)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(character)) {
                hasUppercase = true;
            } else {
                hasSpecial = true;
            }
        }

        // Ensure all categories are present
        while (!(hasDigit && hasLowercase && hasUppercase && hasSpecial)) {
            int randomIndex = random.nextInt(password.length());
            char currentChar = password.charAt(randomIndex);
            categories = categories.replace("" + currentChar, "");
            password.setCharAt(randomIndex, characters.charAt(random.nextInt(characters.length())));

            // Recheck categories
            hasDigit = false;
            hasLowercase = false;
            hasUppercase = false;
            hasSpecial = false;
            for (char c : password.toString().toCharArray()) {
                if (Character.isDigit(c)) {
                    hasDigit = true;
                } else if (Character.isLowerCase(c)) {
                    hasLowercase = true;
                } else if (Character.isUpperCase(c)) {
                    hasUppercase = true;
                } else {
                    hasSpecial = true;
                }
            }
        }

        return password.toString();
    }
}

