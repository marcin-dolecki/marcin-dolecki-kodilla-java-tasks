package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailSchedulerTest {

    private final SimpleEmailService emailService = mock(SimpleEmailService.class);
    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final AdminConfig adminConfig = mock(AdminConfig.class);

    private final EmailScheduler emailScheduler = new EmailScheduler(emailService, taskRepository, adminConfig);

    @Test
    void shouldSendEmailWithTaskCount() {
        when(taskRepository.count()).thenReturn(5L);
        when(adminConfig.getAdminMail()).thenReturn("admin@test.com");
        ArgumentCaptor<Mail> mailCaptor = ArgumentCaptor.forClass(Mail.class);

        emailScheduler.sendInformationEmail();

        verify(emailService, times(1)).send(mailCaptor.capture());
        Mail sentMail = mailCaptor.getValue();

        assertEquals("admin@test.com", sentMail.getMailTo());
        assertEquals("Tasks: Once a day email", sentMail.getSubject());
        assertEquals("Currently in database you got: 5 tasks.", sentMail.getMessage());
    }
}
