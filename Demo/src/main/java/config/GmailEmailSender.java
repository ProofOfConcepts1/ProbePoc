package config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class GmailEmailSender {
	static ConfigFileReader configFileReader;
    public static void emailSent(String subject, String errorMsg) throws IOException, SQLException {
    	
    	configFileReader = new ConfigFileReader();
    	
//    	System.out.println("starting....");
        // Sender's email address and password
//        final String senderEmail = "laksys@gmail.com";
//        final String senderPassword = "nuyhppjelokwlqzr";
    	
        final String senderEmail = "probepoc2023@gmail.com";
        final String senderPassword = "rovqljwppgraopla";

        // Recipient's email address
        String recipientEmail = configFileReader.getTo();
        String recipientEmail1 = configFileReader.getCC();
        
        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new message
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(recipientEmail1));
            
            // Set the subject and content of the email
            message.setSubject(subject);
            message.setText(errorMsg);

            // Send the email
            Transport.send(message);

//            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
//        System.out.println("ending....");
    }
}