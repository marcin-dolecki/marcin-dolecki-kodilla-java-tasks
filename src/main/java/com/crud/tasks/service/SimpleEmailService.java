package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.MailType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleEmailService {

    private final JavaMailSender javaMailSender;
    private final MailCreatorService mailCreatorService;

    public void send(final Mail mail) {
        log.info("Starting email preparation...");
        try {
            javaMailSender.send(createMimeMessage(mail));
            log.info("Email has been sent.");
        } catch (MailException e) {
            log.error("Failed to process email sending: {}", e.getMessage(), e);
        }
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            Optional.ofNullable(mail.getToCc())
                .ifPresent(cc -> safeSetCc(messageHelper, cc));
            messageHelper.setSubject(mail.getSubject());
            String content = setContentBasedOnMailType(mail);
            messageHelper.setText(content, true);
        };
    }

    private void safeSetCc(MimeMessageHelper messageHelper, String cc) {
        try {
            messageHelper.setCc(cc);
        } catch (MessagingException e) {
            log.error("Failed to set cc: {}", cc, e);
        }
    }

    private String setContentBasedOnMailType(final Mail mail) {
        return switch (mail.getMailType()) {
            case DAILY_SUMMARY -> mailCreatorService.buildDailyTasksEmail(mail.getMessage());
            case NEW_TRELLO_CARD -> mailCreatorService.buildTrelloCardEmail(mail.getMessage());
            default -> mail.getMessage();
        };
    }

    /* SUPPORTS ONLY TEXT (NO HTML) --> BASIC VERSION FOR TRAINING PURPOSE (NO PROFESSIONAL USAGE) */
//    private SimpleMailMessage createMailMessage(final Mail mail) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(mail.getMailTo());
//        mailMessage.setSubject(mail.getSubject());
//        mailMessage.setText(mail.getMessage());
//
//        Optional.ofNullable(mail.getToCc())
//                .ifPresent(mailMessage::setCc);
//
//        return mailMessage;
//    }
}
