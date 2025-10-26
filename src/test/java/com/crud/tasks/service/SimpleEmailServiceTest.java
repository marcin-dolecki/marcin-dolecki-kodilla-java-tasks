package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.MailType;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SimpleEmailServiceTest {

    @InjectMocks
    private SimpleEmailService simpleEmailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailCreatorService mailCreatorService;

    @Test
    public void shouldSendEmailWithoutCc() throws Exception {
        // Given
        Mail mail = Mail.builder()
                .mailTo("test@test.com")
                .subject("test")
                .message("test message")
                .mailType(MailType.NEW_TRELLO_CARD)
                .build();

        org.mockito.Mockito.when(mailCreatorService.buildTrelloCardEmail(mail.getMessage()))
                .thenReturn("<html>Test message</html>");

        // When
        simpleEmailService.send(mail);

        // Then
        ArgumentCaptor<MimeMessagePreparator> captor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        MimeMessagePreparator preparator = captor.getValue();
        assertNotNull(preparator);

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        preparator.prepare(mimeMessage);

        assertThat(mimeMessage.getAllRecipients()).hasSize(1);
        assertThat(mimeMessage.getAllRecipients()[0].toString()).isEqualTo("test@test.com");
        assertThat(mimeMessage.getSubject()).isEqualTo("test");

        Object content = mimeMessage.getContent();
        assertThat(content).isInstanceOf(String.class);
        assertThat(((String) content).trim()).isEqualTo("test message");
    }

    @Test
    public void shouldSendEmailWithCc() {
        // Given
        Mail mail = Mail.builder()
                .mailTo("test@test.com")
                .subject("test")
                .message("test message")
                .toCc("cc@test.com")
                .build();

        // When
        simpleEmailService.send(mail);

        // Then
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).containsExactly("test@test.com");
        assertThat(message.getCc()).containsExactly("cc@test.com");
    }
}