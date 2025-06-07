package fun.icystal.act.mail;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
public class MailWithTemplateService {

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void send(String[] to, String subject, String template, Map<String, Object> ctx) {
        Context context = new Context();
        ctx.forEach(context::setVariable);
        String content = templateEngine.process(template, context);
        sendMail(to, subject, content);
    }

    private void sendMail(String[] to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("send email failed", e);
        }
    }

}
