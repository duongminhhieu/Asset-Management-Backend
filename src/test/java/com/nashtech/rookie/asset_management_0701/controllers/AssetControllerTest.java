package com.nashtech.rookie.asset_management_0701.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.Collections;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
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
import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.services.asset.AssetService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

    @Autowired
    private ObjectMapper objectMapper;

    private AssetCreateDto assetCreateDto;
    private AssetResponseDto assetResponseDto;
    private PaginationResponse<AssetResponseDto> assetPagination;

    @BeforeEach
    void setUp() {
        LocalDate localDateTime = LocalDate.now();

        assetCreateDto = AssetCreateDto.builder()
                .name("Asset1")
                .category("Laptop")
                .specification("Specification")
                .installDate(localDateTime)
                .state(EAssetState.AVAILABLE)
                .build();

        assetResponseDto = AssetResponseDto.builder()
                .name("Asset1")
                .category("Laptop")
                .specification("Specification")
                .installDate(localDateTime)
                .state(EAssetState.AVAILABLE)
                .assetCode("LP0001")
                .build();

        assetPagination = PaginationResponse.<AssetResponseDto>builder()
                .data(Collections.singletonList(assetResponseDto))
                .build();
    }

    @Nested
    class HappyCase {
        @Test
        @WithMockUser(roles = "ADMIN")
        void givenAssetCreateDto_whenCreateAsset_thenReturnCreatedAsset() throws Exception {
            // given
            given(assetService.createAsset(any(AssetCreateDto.class))).willReturn(assetResponseDto);

            // when
            ResultActions response = mockMvc.perform(post("/api/v1/assets")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assetCreateDto)));

            // then
            response.andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.result.name").value(assetResponseDto.getName()))
                    .andExpect(jsonPath("$.result.category").value(assetResponseDto.getCategory()))
                    .andExpect(jsonPath("$.result.specification").value(assetResponseDto.getSpecification()))
                    .andExpect(jsonPath("$.result.state")
                            .value(assetResponseDto.getState().name()))
                    .andExpect(jsonPath("$.result.assetCode").value(assetResponseDto.getAssetCode()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getAllAsset_validRequest_returnAssetPagination() throws Exception {
            // GIVEN
            when(assetService.getAllAssets(any())).thenReturn(assetPagination);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("searchString", "")
                            .param("states", "AVAILABLE")
                            .param("categoryIds", "1")
                            .param("orderBy", "name")
                            .param("sortDir", "ASC")
                            .param("pageSize", "10")
                            .param("pageNumber", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.data", hasSize(1)))
                    .andExpect(jsonPath("$.result.data[0].name", is(assetResponseDto.getName())));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getAssetById_validRequest_returnAsset() throws Exception {
            // GIVEN
            when(assetService.getAssetById(any())).thenReturn(assetResponseDto);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assets/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.name", is(assetResponseDto.getName())));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteAssetById_validRequets_returnSuccessMessage() throws Exception {
            // GIVEN
            when(assetService.getAssetById(any())).thenReturn(assetResponseDto);

            // WHEN THEN
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/assets/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("Delete asset successfully")));
        }
    }

    @Nested
    class UnHappyCase {}
}
