package com.nashtech.rookie.asset_management_0701.services;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.repositories.CategoryRepository;
import com.nashtech.rookie.asset_management_0701.services.report.ReportServiceImpl;
import com.nashtech.rookie.asset_management_0701.utils.report.ReportUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ReportServiceImplTest {
    @Autowired
    private ReportServiceImpl reportService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ReportUtil reportUtil;

    @Nested
    class HappyCase {
        @Test
        void adminGetReport_validRequest_returnPaginationResponse() {
            // Given
            List<ReportResponse> mockReportList = Collections.singletonList(new ReportResponse());
            Page<ReportResponse> mockPage = new PageImpl<>(mockReportList, PageRequest.of(0, 10, Sort.by("id")), 1);
            when(categoryRepository.getReport(any(Pageable.class))).thenReturn(mockPage);

            // When
            PaginationResponse<ReportResponse> result = reportService.getReport(1, 10, "id", "asc");

            // Then
            assertEquals(1, result.getPage());
            assertEquals(10, result.getItemsPerPage());
            assertEquals(1, result.getTotal());
            assertEquals(mockReportList, result.getData());
        }

        @Test
        void adminExportReportTest_validRequest_returnResourceDataType() {
            // Given
            List<ReportResponse> mockReportList = Collections.singletonList(new ReportResponse());
            when(categoryRepository.getReport(any(Pageable.class))).thenReturn(new PageImpl<>(mockReportList));

            ByteArrayInputStream mockInputStream = new ByteArrayInputStream(new byte[]{});
            when(reportUtil.writeExcel(mockReportList)).thenReturn(mockInputStream);

            // When
            Resource result = reportService.exportReport("id", "asc");

            // Then
            assertInstanceOf(InputStreamResource.class, result);
            verify(categoryRepository, times(1)).getReport(any(Pageable.class));
            verify(reportUtil, times(1)).writeExcel(mockReportList);
        }
    }
}
