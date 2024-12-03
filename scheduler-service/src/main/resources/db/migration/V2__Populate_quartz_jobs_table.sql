INSERT INTO qrtz_job_details (sched_name, job_name, job_group, description, job_class_name, is_durable, is_nonconcurrent, is_update_data, requests_recovery, job_data)
VALUES ('quartzScheduler', 'TweetServiceFetchUsersJob', 'UserQuartzGroup', null, 'com.gmail.merikbest2015.service.job.TweetServiceFetchUsersJob', true, false, false, false, E'\\xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800');

INSERT INTO qrtz_triggers (sched_name, trigger_name, trigger_group, job_name, job_group, description, next_fire_time, prev_fire_time, priority, trigger_state, trigger_type, start_time, end_time, calendar_name, misfire_instr, job_data)
VALUES ('quartzScheduler', 'TweetServiceFetchUsersJob', 'UserQuartzGroup', 'TweetServiceFetchUsersJob', 'UserQuartzGroup', null, 1733071800000, 1733071740000, 5, 'WAITING', 'CRON', 1733070259000, 0, null, 2, '');

INSERT INTO qrtz_cron_triggers (sched_name, trigger_name, trigger_group, cron_expression, time_zone_id)
VALUES ('quartzScheduler', 'TweetServiceFetchUsersJob', 'UserQuartzGroup', '0 0/5 * * * ?', 'UTC');
