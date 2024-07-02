package com.nashtech.rookie.asset_management_0701.dtos.filters;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.utils.PageSortUtil;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReturningRequestFilter {
    String searchString;

    @Builder.Default
    Set<EAssignmentReturnState> states = Collections.emptySet();

    LocalDate returnDate;

    @Pattern(regexp = "id|assetCode|assetName|requestedBy|assignedDate|acceptedBy|returnDate|state",
        message = "INVALID_SORT_FIELD")
    @Builder.Default
    String orderBy = "id";

    @Pattern(regexp = "ASC|DESC|asc|desc", message = "INVALID_SORT_DIR")
    @Builder.Default
    String sortDir = "ASC";

    @Builder.Default
    Integer pageNumber = 1;

    @Builder.Default
    Integer pageSize = 20;

    public void setPageNumber (String pageNumber) {
        this.pageNumber = PageSortUtil.parsePageValue(pageNumber, 1);
    }

    public void setPageSize (String pageSize) {
        this.pageSize = PageSortUtil.parsePageValue(pageSize, 20);
    }
}