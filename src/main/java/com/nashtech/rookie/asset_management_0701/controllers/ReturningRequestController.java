package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.filters.ReturningRequestFilter;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request.ReturningRequestResponseDto;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/returning-requests")
@RequiredArgsConstructor
public class ReturningRequestController {

    private final ReturningRequestService returningRequestService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<ReturningRequestResponseDto>> getAllReturningRequests (
            @Valid @ModelAttribute ReturningRequestFilter filter) {
        return APIResponse.<PaginationResponse<ReturningRequestResponseDto>>builder()
                .result(returningRequestService.getAllReturningRequests(filter))
                .build();
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> completeReturningRequest (@PathVariable Long id) {
        returningRequestService.completeReturningRequest(id);
        return APIResponse.<String>builder()
                .message("Returning request completed successfully")
                .build();
    }

    @PostMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<ReturningRequestResponseDto> createReturningRequestAtHomePage (@PathVariable("assignmentId")
                                                                                          Long assignmentId) {
        return APIResponse.<ReturningRequestResponseDto>builder()
                .result(returningRequestService.createReturningRequest(assignmentId))
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
    @PostMapping("/demand/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<ReturningRequestResponseDto> createReturningRequestAtAdminPage (@PathVariable("assignmentId")
                                                                                      Long assignmentId) {
        return APIResponse.<ReturningRequestResponseDto>builder()
                .result(returningRequestService.adminCreateReturningRequest(assignmentId))
                .build();
    }
}
