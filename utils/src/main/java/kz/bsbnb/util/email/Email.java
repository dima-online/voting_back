package kz.bsbnb.util.email;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.Date;
import java.util.List;


@Data
@Builder
public class Email {
    @NonNull
    private String from;

    @NonNull
    private String subject;

    @NonNull
    private String body;

    @NonNull
    @Singular("to")
    private List<String> to;

    @Singular("cc")
    private List<String> cc;

    @Singular
    private List<Attachment> attachments;

    public void
    sendWith(JavaMailSender sender) throws Exception {
        MimeMessage mime = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true);

        helper.setTo(this.to.toArray(new String[this.to.size()]));
        helper.setSubject(this.subject);
        helper.setFrom(this.from);
        helper.setText(this.body, true);
        helper.setSentDate(new Date());

        if (cc != null) {
            helper.setCc(this.cc.toArray(new String[this.cc.size()]));
        }

        if (attachments != null) {
            for (Attachment a : this.attachments) {
                //adding files to email as a data source attachments
                DataSource ds = new ByteArrayDataSource(a.getBytes(), "application/octet-stream");
                helper.addAttachment(a.getName(), ds);
            }
        }


        //actual mail sending
        sender.send(mime);
    }
}