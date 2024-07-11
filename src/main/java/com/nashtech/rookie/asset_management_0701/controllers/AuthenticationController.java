package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.auth.AuthenticationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.auth.AuthenticationResponse;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public APIResponse<AuthenticationResponse> login (@RequestBody @Valid AuthenticationRequest request) {

        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.login(request))
                .build();
    }

    @PostMapping("/logout")
    @PreAuthorize("authenticated")
    public APIResponse<String> logout (@RequestHeader("Authorization") String authorizationHeader) {
        final String tokenPrefix = "Bearer ";

        // check if the authorization header is null or does not start with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith(tokenPrefix)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        final String jwtToken = authorizationHeader.replace(tokenPrefix, "");

        authenticationService.logout(jwtToken);

        return APIResponse.<String>builder().message("You have log out successfully").build();
    }
}
