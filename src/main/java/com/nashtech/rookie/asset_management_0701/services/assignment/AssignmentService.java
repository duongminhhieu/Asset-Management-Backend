package com.nashtech.rookie.asset_management_0701.services.assignment;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;

public interface AssignmentService {
    PaginationResponse<AssignmentHistory> getAssignmentHistory (Long assetId, Integer page, Integer size);
}
