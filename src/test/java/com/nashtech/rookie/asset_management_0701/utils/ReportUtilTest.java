package com.nashtech.rookie.asset_management_0701.utils;

import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.utils.report.ReportUtilImpl;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReportUtilTest {
    @Test
    void writeExcelTest() throws IOException {

        Sheet sheet = getRows();

        Row headerRow = sheet.getRow(0);
        assertEquals("Category", headerRow.getCell(0).getStringCellValue());
        assertEquals("Total", headerRow.getCell(1).getStringCellValue());
        assertEquals("Assigned", headerRow.getCell(2).getStringCellValue());
        assertEquals("Available", headerRow.getCell(3).getStringCellValue());
        assertEquals("Not available", headerRow.getCell(4).getStringCellValue());
        assertEquals("Waiting for recycling", headerRow.getCell(5).getStringCellValue());
        assertEquals("Recycled", headerRow.getCell(6).getStringCellValue());

        Row dataRow = sheet.getRow(1);
        assertEquals("Electronics", dataRow.getCell(0).getStringCellValue());
        assertEquals(100, dataRow.getCell(1).getNumericCellValue());
        assertEquals(50, dataRow.getCell(2).getNumericCellValue());
        assertEquals(30, dataRow.getCell(3).getNumericCellValue());
        assertEquals(10, dataRow.getCell(4).getNumericCellValue());
        assertEquals(5, dataRow.getCell(5).getNumericCellValue());
        assertEquals(5, dataRow.getCell(6).getNumericCellValue());
    }

    private static Sheet getRows() throws IOException {
        ReportResponse mockResponse = new ReportResponse();
        mockResponse.setCategoryName("Electronics");
        mockResponse.setTotal(100L);
        mockResponse.setAssignedCount(50L);
        mockResponse.setAvailableCount(30L);
        mockResponse.setNotAvailableCount(10L);
        mockResponse.setWaitingForRecycleCount(5L);
        mockResponse.setRecycledCount(5L);

        List<ReportResponse> mockResponses = Collections.singletonList(mockResponse);

        ReportUtilImpl reportUtil = new ReportUtilImpl();

        ByteArrayInputStream result = reportUtil.writeExcel(mockResponses);

        Workbook workbook = new XSSFWorkbook(result);
        Sheet sheet = workbook.getSheetAt(0);
        return sheet;
    }
}
