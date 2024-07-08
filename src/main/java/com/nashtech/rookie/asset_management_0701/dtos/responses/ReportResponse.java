package com.nashtech.rookie.asset_management_0701.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    Long categoryId;
    String categoryName;
    Long total;
    Long assignedCount;
    Long availableCount;
    Long notAvailableCount;
    Long waitingForRecycleCount;
    Long recycledCount;
}
