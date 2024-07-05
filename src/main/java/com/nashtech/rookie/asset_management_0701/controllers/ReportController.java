package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.services.ReportService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<ReportResponse>> getReport (
            @RequestParam(defaultValue = "1") Integer pageNumber
            , @RequestParam(defaultValue = "10") Integer pageSize
            , @RequestParam(defaultValue = "id") String orderBy
            , @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return APIResponse.<PaginationResponse<ReportResponse>>builder()
                .result(reportService.getReport(pageNumber, pageSize, orderBy, sortDir))
                .build();
    }
}
