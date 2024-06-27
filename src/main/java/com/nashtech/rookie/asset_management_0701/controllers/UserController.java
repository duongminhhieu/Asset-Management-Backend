package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.ChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.FirstChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserSearchDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<UserResponse> createUser (@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return APIResponse.<UserResponse>builder().result(userResponse).build();
    }

    @GetMapping("/generate-username")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<String> getUsernameGenerated (@RequestParam("firstName") String firstName,
                                                     @RequestParam("lastName") String lastName) {
        var result = userService.generateUsername(firstName, lastName);
        return APIResponse.<String>builder().result(result).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<UserResponse>> getAllUsers (@Valid @ModelAttribute UserSearchDto dto) {
        var result = userService.getAllUser(dto);
        return APIResponse.<PaginationResponse<UserResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<PaginationResponse<UserResponse>> getAllUsersAssignment (
            @Valid @ModelAttribute UserSearchDto dto) {
        var result = userService.getAllUserAssignment(dto);
        return APIResponse.<PaginationResponse<UserResponse>>builder()
                .result(result)
                .build();
    }

    @PatchMapping("/first-change-password")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<String> firstChangePassword (
            @RequestBody @Valid FirstChangePasswordRequest firstChangePasswordRequest) {
        userService.firstChangePassword(firstChangePasswordRequest);
        return APIResponse.<String>builder().message("Change password is success").build();
    }

    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<String> changePassword (@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return APIResponse.<String>builder().message("Change password is success").build();
    }
}
