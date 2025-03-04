package com.example.demo_clothes_shop_23.service;

import com.example.demo_clothes_shop_23.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@AllArgsConstructor
public class MailService {
    private final JavaMailSender sender;
    private final TemplateEngine templateEngine;

    public void sendMail2(User user, String subject, String link) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Set encoding to UTF-8
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            final Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("link", link);

            // Create the HTML body using Thymeleaf
            final String htmlContent = templateEngine.process("mail/template-register.html", context);
            helper.setText(htmlContent, true); // Use helper.setText to set the content and indicate it's HTML

            // Send Message!
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMail3(User user, String subject, String link) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Set encoding to UTF-8
            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            final Context context = new Context();
            context.setVariable("user", user);
            context.setVariable("link", link);

            // Create the HTML body using Thymeleaf
            final String htmlContent = templateEngine.process("mail/template-forgetPassword.html", context);
            helper.setText(htmlContent, true); // Use helper.setText to set the content and indicate it's HTML

            // Send Message!
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
