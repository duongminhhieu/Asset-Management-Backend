package com.nashtech.rookie.asset_management_0701.dtos.responses.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private Long id;
    private String name;
    private String code;
}
