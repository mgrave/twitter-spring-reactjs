package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.constants.SchedulerErrorMessage;
import com.gmail.merikbest2015.constants.SchedulerSuccessMessage;
import com.gmail.merikbest2015.dto.request.JobRequest;
import com.gmail.merikbest2015.service.SchedulerService;
import com.gmail.merikbest2015.service.job.JobClassRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final Scheduler scheduler;
    private final JobClassRegistry jobClassRegistry;

    @PostConstruct
    public void startSchedule() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public List<JobDetail> getJobs() {
        return scheduler.getJobKeys(GroupMatcher.anyGroup()).stream()
                .map(jobKey -> getJob(jobKey.getName(), jobKey.getGroup()))
                .toList();
    }

    @Override
    @SneakyThrows
    public JobDetail getJob(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        validateJobKey(jobKey);
        return scheduler.getJobDetail(jobKey);
    }

    @Override
    @SneakyThrows
    public List<? extends Trigger> getJobTriggers(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        validateJobKey(jobKey);
        return scheduler.getTriggersOfJob(jobKey);
    }

    @Override
    @Transactional
    @SneakyThrows
    public Trigger createJob(JobRequest jobRequest) {
        Class<? extends Job> jobClass = jobClassRegistry.getJobClass(jobRequest.getJobClassName());
        JobKey jobKey = new JobKey(jobClass.getSimpleName(), jobRequest.getGroupName());
        if (scheduler.checkExists(jobKey)) {
            throw new ApiRequestException(SchedulerErrorMessage.JOB_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .storeDurably()
                .build();
        CronScheduleBuilder cronSchedule = getCronSchedule(jobRequest.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobKey.getName(), jobRequest.getGroupName())
                .withSchedule(cronSchedule)
                .startNow()
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        return trigger;
    }

    @Override
    @Transactional
    @SneakyThrows
    public Trigger updateJob(JobRequest jobRequest) {
        Class<? extends Job> jobClass = jobClassRegistry.getJobClass(jobRequest.getJobClassName());
        JobKey jobKey = new JobKey(jobClass.getSimpleName(), jobRequest.getGroupName());
        validateJobKey(jobKey);
        TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobRequest.getGroupName());
        CronScheduleBuilder cronSchedule = getCronSchedule(jobRequest.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobKey.getName(), jobRequest.getGroupName())
                .withSchedule(cronSchedule)
                .startNow()
                .build();
        scheduler.rescheduleJob(triggerKey, trigger);
        return trigger;
    }

    @Override
    @Transactional
    @SneakyThrows
    public String deleteJob(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        validateJobKey(jobKey);
        scheduler.deleteJob(jobKey);
        return SchedulerSuccessMessage.JOB_DELETED;
    }

    @SneakyThrows
    private void validateJobKey(JobKey jobKey) {
        if (!scheduler.checkExists(jobKey)) {
            throw new ApiRequestException(SchedulerErrorMessage.JOB_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private static CronScheduleBuilder getCronSchedule(String cronExpression) {
        return CronScheduleBuilder.cronSchedule(cronExpression)
                .withMisfireHandlingInstructionDoNothing()
                .inTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
    }
}
