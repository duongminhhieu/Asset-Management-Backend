package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.auth.AuthenticationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.auth.AuthenticationResponse;
import com.nashtech.rookie.asset_management_0701.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public APIResponse<AuthenticationResponse> login (@Valid @RequestBody AuthenticationRequest request) {

        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.login(request))
                .build();
    }
}
