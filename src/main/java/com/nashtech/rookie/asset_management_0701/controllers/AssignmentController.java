package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.filters.AssignmentFilter;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentUpdateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
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
            , @RequestParam(defaultValue = "10") Integer pageSize) {
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<AssignmentResponse> getAssignment (@PathVariable("id") Long id) {
        return APIResponse.<AssignmentResponse>builder()
                .result(assignmentService.getAssignment(id))
                .build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<AssignmentResponseDto> updateAssignment (
            @PathVariable("id") Long id,
            @Valid @RequestBody AssignmentUpdateDto assignmentUpdateDto) {

        return APIResponse.<AssignmentResponseDto>builder()
                .result(assignmentService.updateAssignment(id, assignmentUpdateDto))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> deleteAssignment (@PathVariable Long id) {
        assignmentService.deleteAssignment(id);

        return APIResponse.<String>builder()
                .message("Assignment deleted successfully")
                .build();
    }
    @GetMapping("/me")
    public APIResponse<PaginationResponse<AssignmentResponseDto>> getMyAssignments (
            @Valid @ModelAttribute AssignmentFilter assignmentFilter){
        return APIResponse.<PaginationResponse<AssignmentResponseDto>>builder()
                .result(assignmentService.getMyAssignments(assignmentFilter))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<AssignmentResponseDto>> getAllAssignment (
        @Valid @ModelAttribute AssignmentFilter filter){
        return APIResponse.<PaginationResponse<AssignmentResponseDto>>builder()
                .result(assignmentService.getAllAssignments(filter))
                .build();
    }

    @PatchMapping("/{id}")
    public APIResponse<AssignmentResponseDto> changeAssignmentState (
            @PathVariable Long id,
            @RequestParam("state") EAssignmentState state) {
        return APIResponse.<AssignmentResponseDto>builder()
                .result(assignmentService.changeState(id, state))
                .build();
    }
}
