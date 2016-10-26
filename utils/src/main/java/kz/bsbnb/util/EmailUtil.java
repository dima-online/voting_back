package kz.bsbnb.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * Created by ruslan on 19.10.16.
 */
public class EmailUtil {

    private static final String SMTP_AUTH_USER;
    private static final String SMTP_AUTH_PWD;
    public static final String MAIL_SENDER = "no-reply@fin-apps.com";
    public static final String MAIL_SENDER_PWD = "ONhNVd0j";

    private static final Properties properties;

    static {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.yandex.ru");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        SMTP_AUTH_USER = MAIL_SENDER;
        SMTP_AUTH_PWD = MAIL_SENDER_PWD;

        System.out.println("EMAIL API INITIALIZED SUCCESSFULLY!!!");
    }

    public static void send(List<String> recipients, String subject, String body) {

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            }
        });
        try {
            System.out.println(SMTP_AUTH_PWD);
            System.out.println(SMTP_AUTH_USER);
            Transport transport = session.getTransport("smtp");

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_AUTH_USER));

            List<Address> addresses = new ArrayList<>();
            for (String recipient : recipients) {
                addresses.add(new InternetAddress(recipient));
            }
            message.addRecipients(Message.RecipientType.TO, (Address[]) addresses.toArray(new Address[0]));
            message.setSubject(subject);
            message.setContent(body, "text/plain; charset=utf-8");

            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();

            System.out.println("Email sent ot address = [" + addresses.toArray(new Address[0]) + "]");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void sendHtml(List<String> recipients, String subject, String body) {

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            }
        });
        try {
            System.out.println(SMTP_AUTH_PWD);
            System.out.println(SMTP_AUTH_USER);
            Transport transport = session.getTransport("smtp");

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_AUTH_USER));

            List<Address> addresses = new ArrayList<>();
            for (String recipient : recipients) {
                addresses.add(new InternetAddress(recipient));
            }
            message.addRecipients(Message.RecipientType.TO, (Address[]) addresses.toArray(new Address[0]));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();

            System.out.println("Email sent ot address = [" + addresses.toArray(new Address[0]) + "]");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
