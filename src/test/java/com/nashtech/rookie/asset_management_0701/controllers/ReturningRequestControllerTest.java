package com.nashtech.rookie.asset_management_0701.controllers;

import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReturningRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ReturningRequestService returningRequestService;


    @BeforeEach
    void setUp() {

    }


    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void cancelReturningRequest_validRequest_success() throws Exception {
            // GIVEN
            Long id = 1L;
            doNothing().when(returningRequestService).cancelReturningRequest(id);

            // WHEN, THEN
            mockMvc.perform(delete("/api/v1/returning-requests/" + id))
                    .andExpect(status().isOk());
        }

    }


    @Nested
    class UnhappyCase {
    }
}
