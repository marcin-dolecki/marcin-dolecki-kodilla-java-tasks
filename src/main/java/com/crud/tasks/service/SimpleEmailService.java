package com.crud.tasks.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEmailService {

    private final JavaMailSender javaMailSender;

    public void send(final String receiverEmail, final String subject, final String message) {

        log.info("Starting email preparation...");

        try {
            SimpleMailMessage mailMessage = createMailMessage(receiverEmail, subject, message);
            javaMailSender.send(mailMessage);
            log.info("Email has been sent.");
        } catch (MailException e) {
            log.error("Failed to process email sending: {}", e.getMessage(), e);
        }
    }

    private SimpleMailMessage createMailMessage(final String receiverEmail, final String subject, final String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        return mailMessage;
    }
}
