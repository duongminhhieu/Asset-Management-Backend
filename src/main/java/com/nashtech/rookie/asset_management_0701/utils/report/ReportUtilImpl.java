package com.nashtech.rookie.asset_management_0701.utils.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;

@Component
public class ReportUtilImpl implements ReportUtil{
    @Override
    public ByteArrayInputStream writeExcel (List<ReportResponse> responses) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Assets report");
            sheet.setColumnWidth(4, 3000); // Not available
            sheet.setColumnWidth(5, 5000); // Waiting for recycling

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category");
            headerRow.createCell(1).setCellValue("Total");
            headerRow.createCell(2).setCellValue("Assigned");
            headerRow.createCell(3).setCellValue("Available");
            headerRow.createCell(4).setCellValue("Not available");
            headerRow.createCell(5).setCellValue("Waiting for recycling");
            headerRow.createCell(6).setCellValue("Recycled");

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Create font for header
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                headerRow.getCell(i).setCellStyle(headerCellStyle);
            }

            for (int i = 0; i < responses.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(responses.get(i).getCategoryName());
                row.createCell(1).setCellValue(responses.get(i).getTotal());
                row.createCell(2).setCellValue(responses.get(i).getAssignedCount());
                row.createCell(3).setCellValue(responses.get(i).getAvailableCount());
                row.createCell(4).setCellValue(responses.get(i).getNotAvailableCount());
                row.createCell(5).setCellValue(responses.get(i).getWaitingForRecycleCount());
                row.createCell(6).setCellValue(responses.get(i).getRecycledCount());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
        catch (IOException e) {
            throw new AppException(ErrorCode.EXPORT_REPORT_ERROR);
        }
    }
}
