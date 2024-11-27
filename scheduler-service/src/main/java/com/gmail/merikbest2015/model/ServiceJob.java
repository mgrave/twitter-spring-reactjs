package com.gmail.merikbest2015.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "service_jobs")
public class ServiceJob {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_jobs_seq")
    @SequenceGenerator(name = "service_jobs_seq", sequenceName = "service_jobs_seq", initialValue = 100, allocationSize = 1)
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default false")
    private boolean active;
}
