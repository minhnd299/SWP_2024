/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package send_mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ADMIN
 */
public class SendMail {

    public boolean sendMailErrol(String email,String username, String content) {
        boolean test = false;
        final String from = "doantrongthaik47a2@gmail.com";
        final String password = "dykeshnjtvluelxh";

        // Properties : Declare all Attribute:
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP HOST
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // create Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(from, password);
            }
        };

        //Work Session:
        Session session = Session.getInstance(props, auth);

        // Sent email for user:
        final String to = email;
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

            //Sender email:
            msg.setFrom(from);

            //Receiver email:
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email, false));

            //Title Email:
            msg.setSubject("Successfully register course");

            //Set date which you want to sent to user:
            msg.setSentDate(new Date());

            //Set another email to reply:
//            msg.setReplyTo(InternetAddress.parse(from, false));
            // Content of mail:
            msg.setText("""
                        You have invited join out system: 
                        username: """ + username + " \n password: " + content, "UTF-8");

            //Sent Email to User:
            Transport.send(msg);

            test = true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return test;
    }
}
