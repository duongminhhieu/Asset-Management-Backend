package com.nashtech.rookie.asset_management_0701.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookie.asset_management_0701.dtos.requests.location.LocationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.services.location.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationService locationService;

    private LocationRequest locationRequest;

    private LocationResponse locationResponse;

    private List<LocationResponse> locationResponseList;

    @BeforeEach
    void setUp() {
        locationRequest = LocationRequest.builder()
                .name("Tay Ninh")
                .code("TN")
                .build();

        locationResponse = LocationResponse.builder()
                .id(4L)
                .name("Tay Ninh")
                .code("TN")
                .build();

        locationResponseList = List.of(locationResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenValidLocationRequest_WhenCreateLocation_thenReturnCreateLocation() throws Exception {
        given(locationService.createLocation(any(LocationRequest.class))).willReturn(locationResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/locations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationRequest)));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(locationResponseList.getFirst().getId()))
                .andExpect(jsonPath("$.result.name").value(locationResponse.getName()))
                .andExpect(jsonPath("$.result.code").value(locationResponse.getCode()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenValidLocationRequest_WhenGetAllLocations_thenReturnGetAllLocations() throws Exception {
        given(locationService.getAllLocation()).willReturn(locationResponseList);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/locations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationRequest)));

        resultActions.andDo(print())
                .andExpect(jsonPath("$.result[0].id").value(locationResponseList.getFirst().getId()))
                .andExpect(jsonPath("$.result[0].code").value(locationResponseList.getFirst().getCode()))
                .andExpect(jsonPath("$.result[0].name").value(locationResponseList.getFirst().getName()));
    }
}
