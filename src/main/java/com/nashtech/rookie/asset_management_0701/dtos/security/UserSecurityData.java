package com.nashtech.rookie.asset_management_0701.dtos.security;

import java.util.Set;

import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSecurityData {
    private Long id;
    private EUserStatus status;
    private Set<String> invalidTokens;
}
