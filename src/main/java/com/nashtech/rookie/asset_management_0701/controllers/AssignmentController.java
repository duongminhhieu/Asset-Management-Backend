package com.nashtech.rookie.asset_management_0701.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.services.assignment.AssignmentService;
import jakarta.validation.Valid;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<AssignmentResponseDto> createAssignment (
            @Valid @RequestBody AssignmentCreateDto assignmentCreateDto) {
        return APIResponse.<AssignmentResponseDto>builder()
                .result(assignmentService.createAssignment(assignmentCreateDto))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> deleteAssignment (@PathVariable Long id){
        assignmentService.deleteAssignment(id);

        return APIResponse.<String>builder()
                .message("Assignment deleted successfully")
                .build();
    }
}
