package util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
    
    private static final String FROM_EMAIL = "xuankhanh036@gmail.com";
    private static final String APP_PASSWORD = "ksdt vlmm xazr jjzu";
    
    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
        
        return Session.getInstance(props, auth);
    }
    
    public static boolean sendPlainText(String to, String subject, String body) {
        try {
            Session session = createSession();
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/plain; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setText(body, "UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean sendVerificationEmail(String to, String verifyCode) {
        String subject = "Mã xác nhận đặt lại mật khẩu - Hệ Thống Bán Thẻ Điện Thoại";
        String body = "Bạn đã yêu cầu đặt lại mật khẩu. Mã xác nhận của bạn là: " + verifyCode + "\n\nMã này có hiệu lực trong 10 phút.";
        return sendPlainText(to, subject, body);
    }
}

