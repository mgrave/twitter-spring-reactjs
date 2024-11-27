CREATE SEQUENCE service_jobs_seq START 100 INCREMENT 1;

CREATE TABLE service_jobs
(
    id              INT8         NOT NULL,
    job_name        VARCHAR(255) NOT NULL,
    cron_expression VARCHAR(255) NOT NULL,
    active          BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);
