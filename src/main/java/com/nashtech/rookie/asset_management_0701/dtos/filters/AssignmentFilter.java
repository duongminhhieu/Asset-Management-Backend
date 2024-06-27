package com.nashtech.rookie.asset_management_0701.dtos.filters;

import com.nashtech.rookie.asset_management_0701.utils.PageSortUtil;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentFilter {
    @Pattern(regexp = "assetCode|assetName|category|assignedDate|state", message = "INVALID_SORT_FIELD")
    @Builder.Default
    String orderBy = "assignedDate";

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
