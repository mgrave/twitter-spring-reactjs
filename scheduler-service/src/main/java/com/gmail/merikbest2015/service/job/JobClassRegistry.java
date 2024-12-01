package com.gmail.merikbest2015.service.job;

import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.constants.SchedulerErrorMessage;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobClassRegistry {

    private final Map<String, Class<? extends Job>> jobClassMap = new HashMap<>();

    public JobClassRegistry(ApplicationContext context) {
        Map<String, Job> jobBeans = context.getBeansOfType(Job.class);
        jobBeans.forEach((beanName, jobBean) -> jobClassMap.put(jobBean.getClass().getSimpleName(), jobBean.getClass()));
    }

    @SneakyThrows
    public Class<? extends Job> getJobClass(String jobClassName) {
        if (!jobClassMap.containsKey(jobClassName)) {
            throw new ApiRequestException(String.format(SchedulerErrorMessage.JOB_CLASS_NOT_FOUND, jobClassName), HttpStatus.NOT_FOUND);
        }
        return jobClassMap.get(jobClassName);
    }
}
