package com.nashtech.rookie.asset_management_0701.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
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
import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.services.category.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private CategoryCreateDto categoryCreateDto;

    @BeforeEach
    void setUp() {
        // Prepare mock data
        categoryCreateDto = CategoryCreateDto.builder().name("name").code("N1").build();
    }

    @Nested
    class HappyCase {
        @Test
        @WithMockUser(roles = "ADMIN")
        void givenCategoryCreateDto_whenCreateNewCategory_returnCreateOk() throws Exception {
            CategoryResponseDto categoryResponseDto =
                    CategoryResponseDto.builder().name("name").code("N1").build();
            // Mock behavior of categoryService
            given(categoryService.createCategory(any(CategoryCreateDto.class))).willReturn(categoryResponseDto);

            // Call the controller method
            ResultActions response = mockMvc.perform(post("/api/v1/categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoryCreateDto)));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.result.name").value("name"))
                    .andExpect(jsonPath("$.result.code").value("N1"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void whenGetAllCategory_returnCategories() throws Exception {
            List<CategoryResponseDto> categoryResponseDtoList = new LinkedList<>();
            CategoryResponseDto categoryResponseDto1 =
                    CategoryResponseDto.builder().name("Category1").code("C1").build();
            CategoryResponseDto categoryResponseDto2 =
                    CategoryResponseDto.builder().name("Category2").code("C2").build();
            categoryResponseDtoList.add(categoryResponseDto1);
            categoryResponseDtoList.add(categoryResponseDto2);

            // Mock behavior of categoryService
            given(categoryService.getAllCategories()).willReturn(categoryResponseDtoList);

            // Call the controller method
            ResultActions response =
                    mockMvc.perform(get("/api/v1/categories").with(csrf()).contentType(MediaType.APPLICATION_JSON));

            // Verify response
            response.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result", hasSize(categoryResponseDtoList.size())))
                    .andExpect(jsonPath("$.result[0].name", is(categoryResponseDto1.getName())))
                    .andExpect(jsonPath("$.result[0].code", is(categoryResponseDto1.getCode())))
                    .andExpect(jsonPath("$.result[1].name", is(categoryResponseDto2.getName())))
                    .andExpect(jsonPath("$.result[1].code", is(categoryResponseDto2.getCode())));
        }
    }

    @Nested
    class UnHappyCase {
        @Test
        @WithMockUser(roles = "ADMIN")
        void givenCategoryCreateDtoWithDuplicateName_whenCreateNewCategory_returnCreateException() throws Exception {
            // Mock behavior of categoryService
            given(categoryService.createCategory(any(CategoryCreateDto.class)))
                    .willThrow(new AppException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTED));

            // Call the controller method
            ResultActions response = mockMvc.perform(post("/api/v1/categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoryCreateDto)));

            // Verify response
            response.andDo(print()).andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void givenCategoryCreateDtoWithDuplicatePrefix_whenCreateNewCategory_returnCreateException() throws Exception {
            // Mock behavior of categoryService
            given(categoryService.createCategory(any(CategoryCreateDto.class)))
                    .willThrow(new AppException(ErrorCode.CATEGORY_PREFIX_ALREADY_EXISTED));

            // Call the controller method
            ResultActions response = mockMvc.perform(post("/api/v1/categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoryCreateDto)));

            // Verify response
            response.andDo(print()).andExpect(status().isBadRequest());
        }
    }
}
