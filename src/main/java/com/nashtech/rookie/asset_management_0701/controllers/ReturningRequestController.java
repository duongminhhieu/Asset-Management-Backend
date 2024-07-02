package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/returning-requests")
@RequiredArgsConstructor
public class ReturningRequestController {

    private final ReturningRequestService returningRequestService;

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> completeReturningRequest (@PathVariable Long id) {
        returningRequestService.completeReturningRequest(id);
        return APIResponse.<String>builder()
                .message("Returning request completed successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> cancelReturningRequest (@PathVariable Long id) {
        returningRequestService.cancelReturningRequest(id);
        return APIResponse.<String>builder()
                .message("Returning request canceled successfully")
                .build();
    }
}
