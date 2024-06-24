package com.nashtech.rookie.asset_management_0701.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.services.assignment.AssignmentService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/{assetId}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<AssignmentHistory>> getAssignmentHistory (
                                                        @PathVariable Long assetId
                                                        , @RequestParam(defaultValue = "1") Integer pageNumber
                                                        , @RequestParam(defaultValue = "10") Integer pageSize){
        return APIResponse.<PaginationResponse<AssignmentHistory>>builder()
                .result(assignmentService.getAssignmentHistory(assetId, pageNumber, pageSize))
                .build();
    }
}
