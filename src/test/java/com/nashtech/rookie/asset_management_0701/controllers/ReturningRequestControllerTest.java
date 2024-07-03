package com.nashtech.rookie.asset_management_0701.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request.ReturningRequestResponseDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReturningRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReturningRequestService returningRequestService;

    private ReturningRequestResponseDto returningRequestResponseDto;

    @BeforeEach
    void setUp() {
        AssignmentResponseDto assignmentResponseDto;

        AssignmentResponseDto.builder()
                .id(1L)
                .asset(AssetResponseDto.builder()
                        .id("1")
                        .name("Asset1")
                        .state(EAssetState.AVAILABLE)
                        .installDate(LocalDate.now())
                        .specification("Specification")
                        .build())
                .assignedDate(LocalDate.now())
                .assignTo("User1")
                .assignBy("User2")
                .state(EAssignmentState.ACCEPTED)
                .build();

        User user = User.builder()
                .id(1L)
                .build();

        Asset asset = Asset.builder()
                .id(1L)
                .state(EAssetState.AVAILABLE)
                .build();

        Assignment assignment = Assignment.builder()
                .id(1L)
                .state(EAssignmentState.WAITING)
                .assignedDate(LocalDate.now())
                .assignBy(user)
                .assignTo(user)
                .asset(asset)
                .build();

        ReturningRequest.builder()
                .id(1L)
                .requestedBy(user)
                .acceptedBy(user)
                .returnDate(LocalDate.now())
                .assignment(assignment)
                .state(EAssignmentReturnState.WAITING_FOR_RETURNING)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .build();

        assignmentResponseDto = AssignmentResponseDto.builder()
                .id(1L)
                .state(EAssignmentState.WAITING)
                .assignBy(user.getUsername())
                .assignTo(user.getUsername())
                .build();

        returningRequestResponseDto = ReturningRequestResponseDto.builder()
                .id(1L)
                .requestedBy(userResponse)
                .acceptedBy(userResponse)
                .returnDate(LocalDate.now())
                .assignment(assignmentResponseDto)
                .state(EAssignmentReturnState.WAITING_FOR_RETURNING)
                .build();
        returningRequestResponseDto = ReturningRequestResponseDto.builder()
                .id(1L)
                .requestedBy(UserResponse.builder()
                        .id(1L)
                        .username("User1")
                        .firstName("User1")
                        .lastName("User1")
                        .build())
                .state(EAssignmentReturnState.WAITING_FOR_RETURNING)
                .assignment(assignmentResponseDto)
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void createReturningRequest_validRequest_success() throws Exception {
            // Given
            Long id = 1L;
            when(returningRequestService.createReturningRequest(1L)).thenReturn(returningRequestResponseDto);

            // When Then
            mockMvc.perform(post("/api/v1/returning-requests/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.result.id").value(1L))
                    .andExpect(jsonPath("$.result.requestedBy.username").value("User1"))
                    .andExpect(jsonPath("$.result.state").value("WAITING_FOR_RETURNING"))
                    .andExpect(jsonPath("$.result.assignment.id").value(1L));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void adminCreateReturningRequest_validRequest_success() throws Exception {
            // Given
            Long id = 1L;
            when(returningRequestService.adminCreateReturningRequest(1L)).thenReturn(returningRequestResponseDto);

            // When Then
            mockMvc.perform(post("/api/v1/returning-requests/demand/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.result.id").value(1L))
                    .andExpect(jsonPath("$.result.requestedBy.username").value("User1"))
                    .andExpect(jsonPath("$.result.state").value("WAITING_FOR_RETURNING"))
                    .andExpect(jsonPath("$.result.assignment.id").value(1L));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void getAllReturningRequests_validRequest_success() throws Exception {
            // GIVEN
            PaginationResponse<ReturningRequestResponseDto> response
                    = PaginationResponse.<ReturningRequestResponseDto>builder()
                    .total(1L)
                    .page(1)
                    .itemsPerPage(10)
                    .data(Collections.singletonList(returningRequestResponseDto))
                    .build();
            when(returningRequestService.getAllReturningRequests(any())).thenReturn(response);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/returning-requests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("orderBy", "assignedDate")
                    .param("sortDir", "ASC")
                    .param("pageSize", "10")
                    .param("pageNumber", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.data", hasSize(1)));
        }

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

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void completeReturningRequest_validRequest_success() throws Exception {
            // GIVEN
            Long id = 1L;
            doNothing().when(returningRequestService).completeReturningRequest(id);

            // WHEN, THEN
            mockMvc.perform(patch("/api/v1/returning-requests/" + id + "/complete"))
                    .andExpect(status().isOk());
        }
    }


    @Nested
    class UnhappyCase {
    }
}
