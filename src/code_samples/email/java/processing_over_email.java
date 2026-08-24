import java.util.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProcessingOverEmail {
    public static boolean sendEmail(
        String receiverEmail,
        String subject,
        String body,
        List<String> attachmentPaths,
        String senderEmail,
        String smtpServer,
        int smtpPort,
        String password
    ) {
        try {
            // Create the email message
            Properties properties = new Properties();
            properties.put("mail.smtp.host", smtpServer);
            properties.put("mail.smtp.port", String.valueOf(smtpPort));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, password);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            message.setSubject(subject);
            
            // Create multipart message
            Multipart multipart = new MimeMultipart();
            
            // Add body to email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);
            
            // Process attachments if any
            if (attachmentPaths != null) {
                for (String attachmentPath : attachmentPaths) {
                    try {
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        File file = new File(attachmentPath);
                        attachmentPart.attachFile(file);
                        multipart.addBodyPart(attachmentPart);
                    } catch (IOException e) {
                        System.out.println("Error processing attachment " + attachmentPath + ": " + e.getMessage());
                        continue;
                    }
                }
            }
            
            message.setContent(multipart);
            
            // Send email
            Transport.send(message);
            
            return true;
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
        List<String> attachments = Arrays.asList("sample.pdf");
        sendEmail(
            "receiver@example.com",
            "Subject",
            "Body",
            attachments,
            "your_email@example.com",
            "smtp.gmail.com",
            587,
            "your_app_password"
        );
    }
}
