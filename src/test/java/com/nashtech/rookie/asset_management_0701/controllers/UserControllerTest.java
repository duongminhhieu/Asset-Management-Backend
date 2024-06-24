package com.nashtech.rookie.asset_management_0701.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.ChangePasswordRequest;
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
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.FirstChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserSearchDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.services.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRequest userRequest;

    private UserResponse userResponse;

    private PaginationResponse<UserResponse> userListResponse;
    private FirstChangePasswordRequest firstChangePasswordRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .firstName("Duy")
                .lastName("Nguyen Hoang")
                .dob(LocalDate.of(2001, 10, 15))
                .gender(EGender.MALE)
                .joinDate(LocalDate.of(2024, 6, 17))
                .build();

        userResponse = UserResponse.builder()
                .firstName("Duy")
                .lastName("Nguyen Hoang")
                .dob(LocalDate.of(2001, 10, 15))
                .gender(EGender.MALE)
                .joinDate(LocalDate.of(2024, 6, 17))
                .build();

        userListResponse = PaginationResponse.<UserResponse>builder()
                .page(1)
                .total(1L)
                .itemsPerPage(20)
                .data(List.of(userResponse))
                .build();
        firstChangePasswordRequest =
                FirstChangePasswordRequest.builder().password("Admin@123").build();
        changePasswordRequest = ChangePasswordRequest.builder()
                .password("Admin@123")
                .newPassword("Admin@1234")
                .build();
    }

    @Nested
    class HappyCase {
        @Test
        @WithMockUser(roles = "ADMIN")
        void givenValidUserRequest_whenCreateUser_thenReturnCreatedUser() throws Exception {
            // Mock behavior of userService
            given(userService.createUser(any(UserRequest.class))).willReturn(userResponse);

            // Call the controller method
            ResultActions response = mockMvc.perform(post("/api/v1/users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest)));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.firstName").value(userResponse.getFirstName()))
                    .andExpect(jsonPath("$.result.lastName").value(userResponse.getLastName()))
                    .andExpect(
                            jsonPath("$.result.dob").value(userResponse.getDob().toString()))
                    .andExpect(jsonPath("$.result.gender")
                            .value(userResponse.getGender().toString()))
                    .andExpect(jsonPath("$.result.joinDate")
                            .value(userResponse.getJoinDate().toString()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void givenFirstNameAndLastName_whenGetUsernameGenerated_thenReturnGeneratedUsername() throws Exception {
            // Given
            String firstName = "Duy";
            String lastName = "Nguyen Hoang";
            String generatedUsername = "duynh";

            // Mock behavior of userService
            given(userService.generateUsername(anyString(), anyString())).willReturn(generatedUsername);

            // Call the controller method
            ResultActions response = mockMvc.perform(get("/api/v1/users/generate-username")
                    .with(csrf())
                    .param("firstName", firstName)
                    .param("lastName", lastName)
                    .contentType(MediaType.APPLICATION_JSON));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value(generatedUsername));
        }

        @Test
        @WithMockUser(roles = "ADMIN", username = "admin")
        void givenValidChangPassWordRequest_whenChangePassword_thenReturnChangedPassword() throws Exception {
            // Mock behavior of userService


            // Call the controller method
            ResultActions response = mockMvc.perform(patch("/api/v1/users/first-change-password")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(firstChangePasswordRequest)));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Change password is success"));

        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testGetAllUsers_whenNotGivenAnyParams_willReturnResponse() throws Exception {
            // given
            when(userService.getAllUser(any(UserSearchDto.class))).thenReturn(userListResponse);
            // when and then
            mockMvc.perform(get("/api/v1/users")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(jsonPath("$.result.total").value(1))
                    .andExpect(jsonPath("$.result.page").value(1))
                    .andExpect(jsonPath("$.result.itemsPerPage").value(20))
                    .andExpect(jsonPath("$.result.data").exists());
        }

        @Test
        @WithMockUser(roles = "ADMIN", username = "admin")
        void testChangePassword_validRequest_success() throws Exception {
            // given

            // when, then
            ResultActions response = mockMvc.perform(patch("/api/v1/users/change-password")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changePasswordRequest)));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Change password is success"));
        }
    }

    @Nested
    class UnHappyCase {
        @Test
        @WithMockUser(roles = "ADMIN", username = "admin")
        void testChangePassword_invalidPassword_fail() throws Exception {
            // given
            changePasswordRequest.setPassword("abc");
            // when, then
            ResultActions response = mockMvc.perform(patch("/api/v1/users/change-password")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changePasswordRequest)));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.internalCode").value(2001))
                    .andExpect(jsonPath("$.message").value("Password must be at least 8 characters less than 128 characters" +
                        " contains at least 1 uppercase, 1 lowercase, 1 special characters, 1 number"))
            ;
        }

    }

}
