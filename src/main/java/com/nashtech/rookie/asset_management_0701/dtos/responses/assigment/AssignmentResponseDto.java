package com.nashtech.rookie.asset_management_0701.dtos.responses.assigment;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
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

    private String asset;

    private LocalDate returnDate;

}
