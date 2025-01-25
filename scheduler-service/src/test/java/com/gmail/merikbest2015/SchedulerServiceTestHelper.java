package com.gmail.merikbest2015;

import com.gmail.merikbest2015.dto.request.JobRequest;
import com.gmail.merikbest2015.dto.response.JobDetailResponse;
import com.gmail.merikbest2015.dto.response.TriggerResponse;

public class SchedulerServiceTestHelper {

    public static JobRequest mockCreateJobRequest() {
        return JobRequest.builder()
                .jobName("TopicServiceFetchUsersJob")
                .groupName("UserQuartzGroup")
                .cronExpression("0 0/5 * * * ?")
                .jobClassName("TopicServiceFetchUsersJob")
                .build();
    }

    public static JobRequest mockUpdateJobRequest() {
        return JobRequest.builder()
                .jobName("TweetServiceFetchUsersJob")
                .groupName("UserQuartzGroup")
                .cronExpression("0 0/5 * * * ?")
                .jobClassName("TweetServiceFetchUsersJob")
                .build();
    }

    public static JobDetailResponse mockTweetJobDetailResponse() {
        return JobDetailResponse.builder()
                .name("TweetServiceFetchUsersJob")
                .group("UserQuartzGroup")
                .jobClass("class com.gmail.merikbest2015.service.job.TweetServiceFetchUsersJo")
                .build();
    }

    public static JobDetailResponse mockChatJobDetailResponse() {
        return JobDetailResponse.builder()
                .name("ChatServiceFetchUsersJob")
                .group("UserQuartzGroup")
                .jobClass("class com.gmail.merikbest2015.service.job.ChatServiceFetchUsersJob")
                .build();
    }

    public static TriggerResponse mockTweetTriggerResponse() {
        return TriggerResponse.builder()
                .triggerName("TweetServiceFetchUsersJob")
                .groupName("UserQuartzGroup")
                .cronExpression("0 0/5 * * * ?")
                .nextFireTime("Sun, 07 Dec 2224 14:55:00 GMT")
                .previousFireTime("Sun, 07 Dec 2224 14:50:00 GMT")
                .build();
    }
}
