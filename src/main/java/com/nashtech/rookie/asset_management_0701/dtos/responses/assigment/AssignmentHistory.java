package com.nashtech.rookie.asset_management_0701.dtos.responses.assigment;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentHistory {

    private Long id;

    private LocalDate assignedDate;

    private String assignTo;

    private String assignBy;

    private LocalDate returnDate;
}
