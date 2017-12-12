package kz.bsbnb.util.email;

import kz.bsbnb.util.UtilResultStep;
import kz.bsbnb.util.processor.MessageProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
@Component
public class MailUtils {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MessageProcessor messageProcessor;

    public void sendMail(String to, String from, String title, String body) {
        Email email = Email.builder()
                .to(to)
                .from(from)
                .subject(title)
                .body(body)
                .build();

        //try to send mail
        try {
            email.sendWith(javaMailSender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UtilResultStep sendMailStep(String to, String from, String title, String body) {
        UtilResultStep step = new UtilResultStep();
        try {
            Email email = Email.builder()
                    .to(to)
                    .from(from)
                    .subject(title)
                    .body(body)
                    .build();

            step.setRequest(email.toString());
            email.sendWith(javaMailSender);
        } catch (Exception e) {
            e.printStackTrace();
            step.setErrorMessage(messageProcessor.getMessage("error.mail.can.not.send", e.getMessage()));
        }
        return step;
    }

    public Boolean sendMailForResult(String to, String from, String title, String body) {
        UtilResultStep step = sendMailStep(to, from, title, body);
        if (StringUtils.isNotBlank(step.getErrorMessage())) {
            return false;
        } else {
            return true;
        }
    }

    public void sendMail(String to, String from, String title, String body, List<Attachment> attachments, String... copy) {
        Email email = Email.builder()
                .to(to)
                .cc(Arrays.asList(copy))
                .from(from)
                .subject(title)
                .body(body)
                .attachments(attachments)
                .build();

        //try to send mail
        try {
            email.sendWith(javaMailSender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMail(String to, String from, String title, String body, String... copy) {
        Email email = Email.builder()
                .to(to)
                .cc(Arrays.asList(copy))
                .from(from)
                .subject(title)
                .body(body)
                .build();

        //try to send mail
        try {
            email.sendWith(javaMailSender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMail(Email email) {
        //try to send mail
        try {
            email.sendWith(javaMailSender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
