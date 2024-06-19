package com.nashtech.rookie.asset_management_0701.dtos.responses.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

    private UserResponse user;

    private String token;
}
