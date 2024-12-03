package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.dto.request.JobRequest;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;

public interface SchedulerService {
    List<JobDetail> getJobs();
    JobDetail getJob(String jobName, String groupName);
    List<? extends Trigger> getJobTriggers(String jobName, String groupName);
    Trigger createJob(JobRequest jobRequest);
    Trigger updateJob(JobRequest jobRequest);
    String deleteJob(String jobName, String groupName);
}
