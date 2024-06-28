package com.nashtech.rookie.asset_management_0701.services.assignment;

import com.nashtech.rookie.asset_management_0701.dtos.filters.AssignmentFilter;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentUpdateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;

public interface AssignmentService {
    PaginationResponse<AssignmentHistory> getAssignmentHistory (Long assetId, Integer page, Integer size);

    void deleteAssignment (Long id);

    PaginationResponse<AssignmentResponseDto> getMyAssignments (AssignmentFilter assignmentFilter);

    AssignmentResponseDto createAssignment (AssignmentCreateDto assignmentCreateDto);

    AssignmentResponseDto updateAssignment (Long id, AssignmentUpdateDto assignmentUpdateDto);

    AssignmentResponse getAssignment (Long id);

    PaginationResponse<AssignmentResponseDto> getAllAssignments (AssignmentFilter assignmentFilter);
}
