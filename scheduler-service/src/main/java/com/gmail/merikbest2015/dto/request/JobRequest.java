package com.gmail.merikbest2015.dto.request;

import com.gmail.merikbest2015.constants.SchedulerErrorMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @NotBlank(message = SchedulerErrorMessage.EMPTY_JOB_NAME)
    private String jobName;

    @NotBlank(message = SchedulerErrorMessage.EMPTY_JOB_GROUP_NAME)
    private String groupName;

    @NotBlank(message = SchedulerErrorMessage.EMPTY_CRON_EXPRESSION)
    private String cronExpression;

    @NotBlank(message = SchedulerErrorMessage.EMPTY_JOB_CLASS_NAME)
    private String jobClassName;
}
