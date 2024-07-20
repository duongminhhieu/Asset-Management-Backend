package com.nashtech.rookie.asset_management_0701.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookie.asset_management_0701.dtos.requests.auth.AuthenticationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.auth.AuthenticationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.services.auth.AuthenticationServiceImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userResponse = UserResponse.builder().firstName("Khoa").lastName("Do").build();

        authenticationRequest = AuthenticationRequest.builder()
                .username("admin")
                .password("123456")
                .build();
        authenticationResponse = AuthenticationResponse.builder()
                .user(userResponse)
                .token("token")
                .build();
    }

    @Nested
    class HappyCase {
        @Test
        void testLogin () throws Exception {
            when(authenticationService.login(authenticationRequest)).thenReturn(authenticationResponse);
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticationRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.user.firstName").value("Khoa"))
                    .andExpect(jsonPath("$.result.user.lastName").value("Do"))
                    .andExpect(jsonPath("$.result.token").value("token"));
        }

//        @Test
//        @WithMockUser // Simulate a logged-in user
//        public void testLogout () throws Exception {
//            String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiQURNSU4iLCJzdGF0dXMiOiJBQ1RJVkUiLCJzdWIiOi" +
//                "JhZG1pbiIsImlhdCI6MTcyMDQyMTMxMCwiZXhwIjoxNzIxMDI2MTEwLCJqdGkiOiJmM2ZjYmVlMy0zODMxLTQ5ZDItYmFjOS1iMj" +
//                "E3YmM1OTUwYmYifQ.ClvYmrUNS59hnXYNi9EaI6Fxx8yyQOEwEepzvn0UON4";
//            // Mocking the logout method of AuthenticationService
//            Mockito.doNothing().when(authenticationService).logout(Mockito.anyString());
//
//            // Perform the POST request to /logout endpoint
//            mockMvc.perform(post("/api/v1/auth/logout")
//                    .header("Authorization", "Bearer " + jwtToken)
//                    .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.message").value("You have log out successfully"));
//
//            // Verify that the logout method was called with the correct token
//            Mockito.verify(authenticationService, Mockito.times(1)).logout(jwtToken);
//        }
    }
}
