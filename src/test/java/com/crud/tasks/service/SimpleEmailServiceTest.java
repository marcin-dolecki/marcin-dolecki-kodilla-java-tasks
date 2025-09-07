package com.crud.tasks.service;

import com.crud.tasks.domain.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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

    @Test
    public void shouldSendEmailWithoutCc() {
        // Given
        Mail mail = Mail.builder()
                .mailTo("test@test.com")
                .subject("test")
                .message("test message")
                .build();

        // When
        simpleEmailService.send(mail);

        // Then
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).containsExactly("test@test.com");
        assertThat(message.getSubject()).isEqualTo("test");
        assertThat(message.getText()).isEqualTo("test message");
        assertThat(message.getCc()).isNull();
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