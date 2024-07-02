package com.nashtech.rookie.asset_management_0701.services.returning_request;

import com.nashtech.rookie.asset_management_0701.dtos.filters.ReturningRequestFilter;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request.ReturningRequestResponseDto;

public interface ReturningRequestService {

    PaginationResponse<ReturningRequestResponseDto> getAllReturningRequests (
            ReturningRequestFilter returningRequestFilter);

    void completeReturningRequest (Long id);
    void cancelReturningRequest (Long id);
}
