package com.nashtech.rookie.asset_management_0701.dtos.responses.assigment;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
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
public class AssignmentResponse {

    private Long id;

    private LocalDate assignedDate;

    private String note;

    @Enumerated(EnumType.STRING)
    private EAssignmentState state;

    private UserResponse assignTo;

    private UserResponse assignBy;

    private AssetResponseDto asset;

    private LocalDate returnDate;

}
