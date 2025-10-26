package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.MailType;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final SimpleEmailService simpleEmailService;
    private final TaskRepository taskRepository;
    private final AdminConfig adminConfig;
    private static final String SUBJECT = "Tasks: Once a day email";

//    @Scheduled(cron = "0 0 10 * * *") // every day at 10
    @Scheduled(fixedDelay = 60000) // every 60 seconds
    public void sendInformationEmail() {

        long size = taskRepository.count();
        simpleEmailService.send(
                Mail.builder()
                        .mailType(MailType.DAILY_SUMMARY)
                        .mailTo(adminConfig.getAdminMail())
                        .subject(SUBJECT)
                        .message("Currently in database you got: " + size + " " + (size == 1 ? "task" : "tasks") + ".")
                        .build()
        );
    }
}
