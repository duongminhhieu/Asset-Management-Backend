package com.nashtech.rookie.asset_management_0701.utils.report;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.nashtech.rookie.asset_management_0701.dtos.responses.ReportResponse;

public interface ReportUtil {
    ByteArrayInputStream writeExcel (List<ReportResponse> responses);
}
