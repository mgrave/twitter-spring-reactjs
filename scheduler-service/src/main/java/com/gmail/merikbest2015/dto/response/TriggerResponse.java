package com.gmail.merikbest2015.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TriggerResponse {
    private String triggerName;
    private String groupName;
    private String cronExpression;
    private String nextFireTime;
    private String previousFireTime;
}
