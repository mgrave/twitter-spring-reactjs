CREATE TABLE qrtz_job_details (
    sched_name        CHARACTER VARYING(120) NOT NULL,
    job_name          CHARACTER VARYING(200) NOT NULL,
    job_group         CHARACTER VARYING(200) NOT NULL,
    description       CHARACTER VARYING(250),
    job_class_name    CHARACTER VARYING(250) NOT NULL,
    is_durable        BOOLEAN                NOT NULL,
    is_nonconcurrent  BOOLEAN                NOT NULL,
    is_update_data    BOOLEAN                NOT NULL,
    requests_recovery BOOLEAN                NOT NULL,
    job_data          BYTEA,
    CONSTRAINT qrtz_job_details_pk PRIMARY KEY (sched_name, job_name, job_group)
);

CREATE TABLE qrtz_triggers (
    sched_name     CHARACTER VARYING(120) NOT NULL,
    trigger_name   CHARACTER VARYING(200) NOT NULL,
    trigger_group  CHARACTER VARYING(200) NOT NULL,
    job_name       CHARACTER VARYING(200) NOT NULL,
    job_group      CHARACTER VARYING(200) NOT NULL,
    description    CHARACTER VARYING(250),
    next_fire_time BIGINT,
    prev_fire_time BIGINT,
    priority       INTEGER,
    trigger_state  CHARACTER VARYING(16)  NOT NULL,
    trigger_type   CHARACTER VARYING(8)   NOT NULL,
    start_time     BIGINT                 NOT NULL,
    end_time       BIGINT,
    calendar_name  CHARACTER VARYING(200),
    misfire_instr  SMALLINT,
    job_data       BYTEA,
    CONSTRAINT qrtz_triggers_pk PRIMARY KEY (sched_name, trigger_name, trigger_group),
    CONSTRAINT qrtz_triggers_fk FOREIGN KEY (job_group, sched_name, job_name)
        REFERENCES qrtz_job_details (job_group, sched_name, job_name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE TABLE qrtz_simple_triggers (
    sched_name      CHARACTER VARYING(120) NOT NULL,
    trigger_name    CHARACTER VARYING(200) NOT NULL,
    trigger_group   CHARACTER VARYING(200) NOT NULL,
    repeat_count    BIGINT                 NOT NULL,
    repeat_interval BIGINT                 NOT NULL,
    times_triggered BIGINT                 NOT NULL,
    CONSTRAINT qrtz_simple_triggers_pk PRIMARY KEY (sched_name, trigger_name, trigger_group),
    CONSTRAINT qrtz_simple_triggers_fk FOREIGN KEY (trigger_name, trigger_group, sched_name)
        REFERENCES qrtz_triggers (trigger_name, trigger_group, sched_name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE TABLE qrtz_simprop_triggers (
    sched_name    CHARACTER VARYING(120) NOT NULL,
    trigger_name  CHARACTER VARYING(200) NOT NULL,
    trigger_group CHARACTER VARYING(200) NOT NULL,
    str_prop_1    CHARACTER VARYING(512),
    str_prop_2    CHARACTER VARYING(512),
    str_prop_3    CHARACTER VARYING(512),
    int_prop_1    INTEGER,
    int_prop_2    INTEGER,
    long_prop_1   BIGINT,
    long_prop_2   BIGINT,
    dec_prop_1    NUMERIC,
    dec_prop_2    NUMERIC,
    bool_prop_1   BOOLEAN,
    bool_prop_2   BOOLEAN,
    time_zone_id  CHARACTER VARYING(80),
    CONSTRAINT qrtz_simprop_triggers_pk PRIMARY KEY (sched_name, trigger_name, trigger_group),
    CONSTRAINT qrtz_simprop_triggers_fk FOREIGN KEY (sched_name, trigger_name, trigger_group)
        REFERENCES qrtz_triggers (sched_name, trigger_name, trigger_group) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE TABLE qrtz_cron_triggers (
    sched_name      CHARACTER VARYING(120) NOT NULL,
    trigger_name    CHARACTER VARYING(200) NOT NULL,
    trigger_group   CHARACTER VARYING(200) NOT NULL,
    cron_expression CHARACTER VARYING(120) NOT NULL,
    time_zone_id    CHARACTER VARYING(80),
    CONSTRAINT qrtz_cron_triggers_pk PRIMARY KEY (sched_name, trigger_name, trigger_group),
    CONSTRAINT qrtz_cron_triggers_fk FOREIGN KEY (trigger_name, trigger_group, sched_name)
        REFERENCES qrtz_triggers (trigger_name, trigger_group, sched_name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE TABLE qrtz_blob_triggers (
    sched_name    CHARACTER VARYING(120) NOT NULL,
    trigger_name  CHARACTER VARYING(200) NOT NULL,
    trigger_group CHARACTER VARYING(200) NOT NULL,
    blob_data     BYTEA,
    CONSTRAINT qrtz_blob_triggers_pk PRIMARY KEY (sched_name, trigger_name, trigger_group),
    CONSTRAINT qrtz_blob_triggers_fk FOREIGN KEY (trigger_name, trigger_group, sched_name)
        REFERENCES qrtz_triggers (trigger_name, trigger_group, sched_name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE TABLE qrtz_calendars (
    sched_name    CHARACTER VARYING(120) NOT NULL,
    calendar_name CHARACTER VARYING(200) NOT NULL,
    calendar      BYTEA                  NOT NULL,
    CONSTRAINT qrtz_calendars_pk PRIMARY KEY (sched_name, calendar_name)
);

CREATE TABLE qrtz_paused_trigger_grps (
    sched_name    CHARACTER VARYING(120) NOT NULL,
    trigger_group CHARACTER VARYING(200) NOT NULL,
    CONSTRAINT qrtz_paused_trigger_grps_pk PRIMARY KEY (sched_name, trigger_group)
);

CREATE TABLE qrtz_fired_triggers (
    sched_name        CHARACTER VARYING(120) NOT NULL,
    entry_id          CHARACTER VARYING(140) NOT NULL,
    trigger_name      CHARACTER VARYING(200) NOT NULL,
    trigger_group     CHARACTER VARYING(200) NOT NULL,
    instance_name     CHARACTER VARYING(200) NOT NULL,
    fired_time        BIGINT                 NOT NULL,
    sched_time        BIGINT                 NOT NULL,
    priority          INTEGER                NOT NULL,
    state             CHARACTER VARYING(16)  NOT NULL,
    job_name          CHARACTER VARYING(200),
    job_group         CHARACTER VARYING(200),
    is_nonconcurrent  BOOLEAN,
    requests_recovery BOOLEAN,
    CONSTRAINT qrtz_fired_triggers_pk PRIMARY KEY (sched_name, entry_id)
);

CREATE TABLE qrtz_scheduler_state (
    sched_name        CHARACTER VARYING(120) NOT NULL,
    instance_name     CHARACTER VARYING(200) NOT NULL,
    last_checkin_time BIGINT                 NOT NULL,
    checkin_interval  BIGINT                 NOT NULL,
    CONSTRAINT qrtz_scheduler_state_pk PRIMARY KEY (sched_name, instance_name)
);

CREATE TABLE qrtz_locks (
    sched_name CHARACTER VARYING(120) NOT NULL,
    lock_name  CHARACTER VARYING(40)  NOT NULL,
    CONSTRAINT qrtz_locks_pk PRIMARY KEY (sched_name, lock_name)
);
