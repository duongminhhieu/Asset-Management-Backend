package com.nashtech.rookie.asset_management_0701.services.returning_request;

public interface ReturningRequestService {

    void completeReturningRequest (Long id);
    void cancelReturningRequest (Long id);
}
