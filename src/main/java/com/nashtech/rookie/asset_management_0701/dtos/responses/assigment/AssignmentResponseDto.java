package com.nashtech.rookie.asset_management_0701.dtos.responses.assigment;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssignmentResponseDto {

    private Long id;

    private LocalDate assignedDate;

    private String note;

    @Enumerated(EnumType.STRING)
    private EAssignmentState state;

    private String assignTo;

    private String assignBy;

    private AssetResponseDto asset;

    private LocalDate returnDate;

}
