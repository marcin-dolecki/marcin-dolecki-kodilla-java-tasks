package com.crud.tasks.service;
import com.crud.tasks.domain.Mail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;

@SpringBootTest
public class SimpleEmailServiceRealTest {

    @Autowired
    private SimpleEmailService simpleEmailService;

    @Test
    public void shouldSendEmail() {
        // Given
        String mailTo = "test@gmail.com";
        Mail mail = new Mail(mailTo, "test", "test message");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());

        // When
        simpleEmailService.send(mail);

        // Then
        System.out.println("The test e-mail has been sent to " + mailTo);
    }
}
