package com.nashtech.rookie.asset_management_0701.services.report;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;
import com.nashtech.rookie.asset_management_0701.repositories.CategoryRepository;
import com.nashtech.rookie.asset_management_0701.utils.PageSortUtil;
import com.nashtech.rookie.asset_management_0701.utils.report.ReportUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportUtil reportUtil;
    private final CategoryRepository categoryRepository;

    @Override
    public PaginationResponse<ReportResponse> getReport (Integer page, Integer pageSize
                                                        , String sortBy, String sortDirection) {
        Sort sort = Sort.by(PageSortUtil.parseSortDirection(sortDirection), sortBy);
        Pageable pageable = PageSortUtil.createPageRequest(page, pageSize, sort);

        Page<ReportResponse> reports = categoryRepository.getReport(pageable);

        return PaginationResponse.<ReportResponse>builder()
                .page(reports.getNumber() + 1)
                .itemsPerPage(reports.getSize())
                .total(reports.getTotalElements())
                .data(reports.getContent())
                .build();
    }

    @Override
    public Resource exportReport () {
        Pageable pageable = Pageable.unpaged();
        List<ReportResponse> reportList = categoryRepository.getReport(pageable).getContent();
        return new InputStreamResource(reportUtil.writeExcel(reportList));
    }
}
