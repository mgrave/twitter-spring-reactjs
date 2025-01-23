package com.gmail.merikbest2015.service.job;

import com.gmail.merikbest2015.client.ChatClient;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatServiceFetchUsersJob implements Job {

    private final ChatClient chatClient;

    @Override
    public void execute(JobExecutionContext context) {
        chatClient.runImportUsersBatchJob();
    }
}
