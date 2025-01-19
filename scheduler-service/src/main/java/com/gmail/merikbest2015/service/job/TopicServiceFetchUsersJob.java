package com.gmail.merikbest2015.service.job;

import com.gmail.merikbest2015.client.TopicClient;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicServiceFetchUsersJob implements Job {

    private final TopicClient topicClient;

    @Override
    public void execute(JobExecutionContext context) {
        topicClient.runImportUsersBatchJob();
    }
}
