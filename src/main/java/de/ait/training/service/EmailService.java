package de.ait.training.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from.address}")
    private String fromAddress;

    @Value("${app.mail.from.personal}")
    private String fromName;

    /**
     * Відправляє HTML email з Thymeleaf-шаблону
     * і автоматично вставляє CSS з файлу styles.css
     */
    public void sendTemplateEmail(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);

            // Створюємо Thymeleaf контекст
            Context context = new Context();
            context.setVariables(variables);

            // Лайтово підтягуємо CSS з файлу і вставляємо у шаблон
            // name: "styles" використовуємо для підключення до html     <style th:utext="${styles}"></style>
            String css = loadCss();
            context.setVariable("styles", css);

            // Рендеримо HTML
            String html = templateEngine.process(template, context);
            helper.setText(html, true);

            mailSender.send(mimeMessage);
            log.info("✅ Email sent to {} with subject '{}'", to, subject);

        } catch (MessagingException | IOException exception) {
            log.error("❌ Failed to send email: {}", exception.getMessage(), exception);
            throw new RuntimeException("Email sending failed", exception);
        }
    }

    /**
     * Зчитує CSS з файлу у ресурсах
     */
    private String loadCss() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email/styles.css");
        return Files.readString(resource.getFile().toPath());
    }
}