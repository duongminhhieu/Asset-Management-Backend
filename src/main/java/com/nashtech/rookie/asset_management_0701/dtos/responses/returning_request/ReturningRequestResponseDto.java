package com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturningRequestResponseDto {

    private Long id;

    private AssignmentResponseDto assignment;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private EAssignmentReturnState state;

    private UserResponse requestedBy;

    private UserResponse acceptedBy;
}
