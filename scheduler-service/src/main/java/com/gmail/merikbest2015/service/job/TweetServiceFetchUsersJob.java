package com.gmail.merikbest2015.service.job;

import com.gmail.merikbest2015.client.TweetClient;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TweetServiceFetchUsersJob implements Job {

    private final TweetClient tweetClient;

    @Override
    public void execute(JobExecutionContext context) {
        tweetClient.runImportUsersBatchJob();
    }
}
