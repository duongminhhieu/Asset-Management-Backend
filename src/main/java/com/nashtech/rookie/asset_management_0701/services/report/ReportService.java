package com.nashtech.rookie.asset_management_0701.services.report;

import org.springframework.core.io.Resource;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;


public interface ReportService {
    PaginationResponse<ReportResponse> getReport (Integer page, Integer pageSize, String sortBy, String sortDirection);

    Resource exportReport ();
}
