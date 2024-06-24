package com.nashtech.rookie.asset_management_0701.controllers;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.services.assignment.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService assignmentService;

    private AssignmentHistory assignmentHistory;

    @BeforeEach
    void setUp() {

        assignmentHistory = AssignmentHistory.builder()
                .assignBy("admin")
                .assignTo("user")
                .assignedDate(LocalDate.now())
                .returnDate(LocalDate.now())
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void getAssignmentHistory_validRequest_success() throws Exception {
            // GIVEN
            long assetId = 1;
            PaginationResponse<AssignmentHistory> assetPagination = PaginationResponse.<AssignmentHistory>builder()
                    .total(1L)
                    .page(1)
                    .itemsPerPage(10)
                    .data(Collections.singletonList(assignmentHistory))
                    .build();
            when(assignmentService.getAssignmentHistory(any(), anyInt(), anyInt())).thenReturn(assetPagination);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assignments/" + assetId + "/history")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("pageSize", "10")
                            .param("pageNumber", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.data", hasSize(1)))
                    .andExpect(jsonPath("$.result.data[0].assignBy", is("admin")));
        }
    }

    @Nested
    class UnhappyCase {
    }

}
