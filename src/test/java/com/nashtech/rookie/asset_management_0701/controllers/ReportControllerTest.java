package com.nashtech.rookie.asset_management_0701.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.services.report.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    private ReportResponse reportResponse;

    @BeforeEach
    void setUp() {
        reportResponse = ReportResponse.builder()
                .categoryId(1L)
                .categoryName("Laptop")
                .total(8L)
                .assignedCount(7L)
                .availableCount(0L)
                .notAvailableCount(1L)
                .waitingForRecycleCount(0L)
                .recycledCount(0L)
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminGetReport_validRequest_success() throws Exception {
        // Given
        PaginationResponse<ReportResponse> response = PaginationResponse.<ReportResponse>builder()
                .total(1L)
                .page(1)
                .itemsPerPage(10)
                .data(Collections.singletonList(reportResponse))
                .build();

        when(reportService.getReport(any(),any(),any(),any())).thenReturn(response);

        // When Then
        mockMvc.perform(get("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("sortBy", "id")
                        .param("sortDir", "ASC")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void exportReport_validRequest_success() throws Exception {
        // Given
        byte[] mockContent = "Test content".getBytes();
        Resource mockResource = new ByteArrayResource(mockContent);

        when(reportService.exportReport(any(), any())).thenReturn(mockResource);

        // When Then
        mockMvc.perform(get("/api/v1/reports/export")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=report.xlsx"))
                .andExpect(content().bytes(mockContent));
    }
}
