package com.gmail.merikbest2015.dto;

import lombok.Data;

@Data
public class ServiceJobRequest {
    private String jobName;
    private String cronExpression;
    private boolean active;
}
