package com.nashtech.rookie.asset_management_0701.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssignmentService assignmentService;

    private AssignmentHistory assignmentHistory;
    private AssignmentCreateDto assignmentCreateDto;
    private AssignmentResponseDto assignmentResponseDto;
    private User user;
    private Asset asset;
    private Assignment assignment;

    @BeforeEach
    void setUp() {

        assignmentHistory = AssignmentHistory.builder()
                .assignBy("admin")
                .assignTo("user")
                .assignedDate(LocalDate.now())
                .returnDate(LocalDate.now())
                .build();

        assignmentCreateDto = AssignmentCreateDto.builder()
                .userId(1L)
                .assetId(1L)
                .assignedDate(LocalDate.now())
                .note("Note")
                .build();

        user = User.builder()
                .id(1L)
                .build();

        asset = Asset.builder()
                .id(1L)
                .state(EAssetState.AVAILABLE)
                .build();

        assignment = Assignment.builder()
                .id(1L)
                .state(EAssignmentState.WAITING)
                .assignBy(user)
                .assignTo(user)
                .asset(asset)
                .build();

        assignmentResponseDto = AssignmentResponseDto.builder()
                .id(1L)
                .assignedDate(LocalDate.now())
                .note("Note")
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

        @Test
        @WithMockUser(roles = "ADMIN")
        void createNewAssignment_validRequest_success() throws Exception {
            // GIVEN
            when(assignmentService.createAssignment(any(AssignmentCreateDto.class))).thenReturn(assignmentResponseDto);

            // WHEN THEN
            mockMvc.perform(post("/api/v1/assignments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(assignmentCreateDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void deleteAssignment_validRequest_success() throws Exception {
            // GIVEN
            long id = 1;

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/assignments/" + id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Assignment deleted successfully")));
        }
    }

    @Nested
    class UnhappyCase {
        @Test
        @WithMockUser(roles = "ADMIN")
        void createNewAssignment_invalidRequest_success() throws Exception {
            // GIVEN
            asset.setState(EAssetState.ASSIGNED);
            when(assignmentService.createAssignment(any(AssignmentCreateDto.class))).thenThrow(new AppException(ErrorCode.ASSET_STATE_NOT_AVAILABLE));

            // WHEN THEN
            mockMvc.perform(post("/api/v1/assignments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(assignmentCreateDto)))
                    .andExpect(status().isBadRequest());
        }
    }

}
