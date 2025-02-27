package utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    // Phương thức để gửi email
    public static void sendEmail(String toEmail, String subject, String messageContent) {
        // Cấu hình các thuộc tính cho email
        Properties properties = new Properties();
        
        // SMTP server configuration (ví dụ sử dụng Gmail SMTP)
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tài khoản email của bạn (sử dụng để gửi email)
        final String fromEmail = "shopshoegroup5@gmail.com";
        final String password = "tjvfwqjygkvihvzi";  // Bạn nên cẩn thận với thông tin này

        // Tạo session để gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Tạo đối tượng email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            
            // Thiết lập nội dung của email
            message.setText(messageContent);

            // Gửi email
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error while sending email to " + toEmail);
        }
    }
}
