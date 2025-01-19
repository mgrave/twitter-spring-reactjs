package com.gmail.merikbest2015.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailResponse {
    private String name;
    private String group;
    private String description;
    private String jobClass;
}
