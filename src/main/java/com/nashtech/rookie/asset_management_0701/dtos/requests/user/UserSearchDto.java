package com.nashtech.rookie.asset_management_0701.dtos.requests.user;

import com.nashtech.rookie.asset_management_0701.constants.DefaultSortOptions;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private String searchString;
    private String type;
    @Pattern(regexp = "fullName|firstName|lastName|username|joinDate|dob|staffCode|type", message= "INVALID_SORT_FIELD")
    private String orderBy = DefaultSortOptions.DEFAULT_USER_SORT_BY;
    @Pattern(regexp = "ASC|DESC|asc|desc", message = "INVALID_SORT_DIR")
    private String sortDir = DefaultSortOptions.DEFAULT_SORT_ORDER;
    @Min(value = 1, message = "PAGE_NUMBER_LESS_THAN_ONE")
    private Integer pageNumber = DefaultSortOptions.DEFAULT_PAGE_NUMBER;
    @Min(value = 1, message = "PAGE_SIZE_LESS_THAN_ONE")
    private Integer pageSize = DefaultSortOptions.DEFAULT_PAGE_SIZE;
}
