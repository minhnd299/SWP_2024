package send_mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import java.security.SecureRandom;
import java.util.Random;
import java.util.ResourceBundle;

public class Mailer {

    private void send(String from, String password, String to, String subject, String messageText) {
        // Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(messageText, "text/html");

            Transport.send(message);
            System.out.println("Message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void welcome(String email) {
        Mailer send = new Mailer();
        ResourceBundle rb = ResourceBundle.getBundle("application");
        String emailId = rb.getString("mailer.email");
        String passWord = rb.getString("mailer.password");
        String from = emailId;
        String password = passWord;

        String to = email;
        String subject = "Welcome";
        String messageText = "" + "<html>" + "<body>"
                + "<h3 style=\"color: rgb(71, 71, 255);\">Welcome, <b style=\"color: black;\">Name</b></h3>\n"
                + "    <p>Welcome to the lab of FBT University. This message is a welcome message, please do not respond. And here is your password to access our website.</p>\n"
                + "    <p>Account: <b>Account</b></p> \n"
                + "    <p>Password: <b>" + generateStrongPassword() + "</b></p> \n"
                + "    <p>Thank you. No reply.</p>\n"
                + "    <p>FBT University.</p>" + "" + "</body>" + "</html>";

        send.send(from, password, to, subject, messageText);
    }

    public String forgetPassword(String email) {
        Mailer send = new Mailer();
        ResourceBundle rb = ResourceBundle.getBundle("application");
        String emailId = rb.getString("mailer.email");
        String passWord = rb.getString("mailer.password");
        String from = emailId;
        String password = passWord;

        String to = email;
        String newPassword = getNewPassword();
        String subject = "Welcome";
        String messageText = "" + "<html>" + "<body>"
                + "<h3>Hello, <b>Name</b></h3>\n"
                + "    <p>This notification is in response to a request to reset your account password. Please visit the website and change your password again.</p>\n"
                + "    <p>Your password is: <b>" + newPassword + "</b></p> \n"
                + "    <p>Thank you.</p>\n"
                + "    <p>FBT University.</p>" + "" + "</body>" + "</html>";

        send.send(from, password, to, subject, messageText);
        return newPassword;
    }

    private String generateStrongPassword() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$*%^&+=-_";
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

    private String getNewPassword() {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*&+=-_])(?=\\S+$).{8}$";
        String password = generateStrongPassword();
        while (!password.matches(regex)) {
            password = generateStrongPassword();
        }
        return password;
    }

}
