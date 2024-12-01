package com.gmail.merikbest2015.mapper;

import com.gmail.merikbest2015.commons.mapper.BasicMapper;
import com.gmail.merikbest2015.dto.response.JobDetailResponse;
import com.gmail.merikbest2015.dto.request.JobRequest;
import com.gmail.merikbest2015.dto.response.TriggerResponse;
import com.gmail.merikbest2015.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.DateUtils;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerMapper {

    private final BasicMapper basicMapper;
    private final SchedulerService schedulerService;

    public List<JobDetailResponse> getJobs() {
        return basicMapper.convertToResponseList(schedulerService.getJobs(), JobDetailResponse.class);
    }

    public JobDetailResponse getJob(String jobName, String groupName) {
        return basicMapper.convertToResponse(schedulerService.getJob(jobName, groupName), JobDetailResponse.class);
    }

    public List<TriggerResponse> getJobTriggers(String jobName, String groupName) {
        return schedulerService.getJobTriggers(jobName, groupName).stream()
                .map(trigger -> {
                    TriggerKey triggerKey = trigger.getKey();
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    return TriggerResponse.builder()
                            .triggerName(triggerKey.getName())
                            .groupName(triggerKey.getGroup())
                            .cronExpression(cronTrigger.getCronExpression())
                            .nextFireTime(DateUtils.formatDate(cronTrigger.getNextFireTime()))
                            .previousFireTime(DateUtils.formatDate(cronTrigger.getPreviousFireTime()))
                            .build();
                })
                .toList();
    }

    public TriggerResponse createJob(JobRequest jobRequest) {
        Trigger trigger = schedulerService.createJob(jobRequest);
        TriggerKey triggerKey = trigger.getKey();
        CronTrigger cronTrigger = (CronTrigger) trigger;
        return TriggerResponse.builder()
                .triggerName(triggerKey.getName())
                .groupName(triggerKey.getGroup())
                .cronExpression(cronTrigger.getCronExpression())
                .build();
    }

    public TriggerResponse updateJob(JobRequest jobRequest) {
        Trigger trigger = schedulerService.updateJob(jobRequest);
        TriggerKey triggerKey = trigger.getKey();
        CronTrigger cronTrigger = (CronTrigger) trigger;
        return TriggerResponse.builder()
                .triggerName(triggerKey.getName())
                .groupName(triggerKey.getGroup())
                .cronExpression(cronTrigger.getCronExpression())
                .build();
    }
}
