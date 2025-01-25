package com.gmail.merikbest2015.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.merikbest2015.SchedulerServiceTestHelper;
import com.gmail.merikbest2015.commons.constants.HeaderConstants;
import com.gmail.merikbest2015.commons.constants.PathConstants;
import com.gmail.merikbest2015.commons.util.TestConstants;
import com.gmail.merikbest2015.constants.SchedulerErrorMessage;
import com.gmail.merikbest2015.constants.SchedulerSuccessMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = {"/sql-test/clear-scheduler-db.sql", "/sql-test/populate-scheduler-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql-test/clear-scheduler-db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SchedulerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("[200] GET /api/v1/scheduler/jobs - Get jobs")
    public void getJobs() throws Exception {
        mockMvc.perform(get(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS)
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("TweetServiceFetchUsersJob"));
    }

    @Test
    @DisplayName("[200] GET /api/v1/scheduler/job/{jobName}/{groupName} - Get job")
    public void getJob() throws Exception {
        mockMvc.perform(get(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_NAME_GROUP_NAME,
                        "TweetServiceFetchUsersJob", "UserQuartzGroup")
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TweetServiceFetchUsersJob"));
    }

    @Test
    @DisplayName("[404] GET /api/v1/scheduler/job/{jobName}/{groupName} - Should job not found")
    public void getJob_ShouldJobNotFound() throws Exception {
        mockMvc.perform(get(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_NAME_GROUP_NAME,
                        "Test", "UserQuartzGroup")
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(SchedulerErrorMessage.JOB_NOT_FOUND)));
    }

    @Test
    @DisplayName("[200] GET /api/v1/scheduler/jobs/triggers/{jobName}/{groupName}- Get job triggers")
    public void getJobTriggers() throws Exception {
        mockMvc.perform(get(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_TRIGGERS_NAME_GROUP_NAME,
                        "TweetServiceFetchUsersJob", "UserQuartzGroup")
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)))
                .andExpect(jsonPath("$[0].triggerName").value("TweetServiceFetchUsersJob"))
                .andExpect(jsonPath("$[0].groupName").value("UserQuartzGroup"));
    }

    @Test
    @DisplayName("[404] GET /api/v1/scheduler/jobs/triggers/{jobName}/{groupName}- Should job trigger not found")
    public void getJobTriggers_ShouldJobNotFound() throws Exception {
        mockMvc.perform(get(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_TRIGGERS_NAME_GROUP_NAME,
                        "Test", "UserQuartzGroup")
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(SchedulerErrorMessage.JOB_NOT_FOUND)));
    }

    @Test
    @DisplayName("[200] POST /api/v1/scheduler/jobs/create - Create job")
    public void createJob() throws Exception {
        mockMvc.perform(post(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_CREATE)
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                        .content(mapper.writeValueAsString(SchedulerServiceTestHelper.mockCreateJobRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.triggerName").value("TopicServiceFetchUsersJob"))
                .andExpect(jsonPath("$.groupName").value("UserQuartzGroup"))
                .andExpect(jsonPath("$.cronExpression").value("0 0/5 * * * ?"));
    }

    @Test
    @DisplayName("[200] PUT /api/v1/scheduler/jobs/update - Update job")
    public void updateJob() throws Exception {
        mockMvc.perform(put(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_UPDATE)
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID)
                        .content(mapper.writeValueAsString(SchedulerServiceTestHelper.mockUpdateJobRequest()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.triggerName").value("TweetServiceFetchUsersJob"))
                .andExpect(jsonPath("$.groupName").value("UserQuartzGroup"))
                .andExpect(jsonPath("$.cronExpression").value("0 0/5 * * * ?"));
    }

    @Test
    @DisplayName("[200] DELETE /api/v1/scheduler/jobs/delete/{jobName}/{groupName} - Delete job")
    public void deleteJob() throws Exception {
        mockMvc.perform(delete(PathConstants.API_V1_SCHEDULER + PathConstants.JOBS_DELETE,
                        "TweetServiceFetchUsersJob", "UserQuartzGroup")
                        .header(HeaderConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(SchedulerSuccessMessage.JOB_DELETED)));
    }
}
