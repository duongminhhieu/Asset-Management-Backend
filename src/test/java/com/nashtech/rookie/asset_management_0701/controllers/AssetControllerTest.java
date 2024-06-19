package com.nashtech.rookie.asset_management_0701.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    @BeforeEach
    void setUp() {
        LocalDateTime localDateTime = LocalDateTime.now();

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
        void whenGetAllAssets_thenReturnAssetList() throws Exception {
            // Mock behavior
            List<AssetResponseDto> assetResponseDtoList = Collections.singletonList(assetResponseDto);
            given(assetService.getAllAssets()).willReturn(assetResponseDtoList);

            // Perform GET request
            ResultActions response =
                    mockMvc.perform(get("/api/v1/assets").with(csrf()).contentType(MediaType.APPLICATION_JSON));

            // Verify response
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.result", hasSize(1)))
                    .andExpect(jsonPath("$.result[0].name", is(assetResponseDto.getName())))
                    .andExpect(jsonPath("$.result[0].category", is(assetResponseDto.getCategory())))
                    .andExpect(jsonPath("$.result[0].assetCode", is(assetResponseDto.getAssetCode())));
        }
    }

    @Nested
    class UnHappyCase {}
}
