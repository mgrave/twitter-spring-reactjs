package com.gmail.merikbest2015.controller.api;

import com.gmail.merikbest2015.commons.constants.PathConstants;
import com.gmail.merikbest2015.commons.util.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = {"/sql-test/clear-tag-db.sql", "/sql-test/populate-tag-db.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql-test/clear-tag-db.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TagApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[200] GET /api/v1/tags/search/test - Get tags by text")
    public void getTagsByText() throws Exception {
        mockMvc.perform(get(PathConstants.API_V1_TAGS + PathConstants.SEARCH_TEXT, "test")
                        .header(PathConstants.AUTH_USER_ID_HEADER, TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)))
                .andExpect(jsonPath("$[0]").value("#test"));
    }
}
