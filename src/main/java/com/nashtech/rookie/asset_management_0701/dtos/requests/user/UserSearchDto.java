package com.nashtech.rookie.asset_management_0701.dtos.requests.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private String searchString;
    private String type;
    private String orderBy = "firstName";
    private String sortDir = "ASC";
    private Integer pageNumber = 1;
    private Integer pageSize = 20;
}
