package com.nashtech.rookie.asset_management_0701.services.user;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.ChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.FirstChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserSearchDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;

public interface UserService {
    UserResponse createUser (UserRequest userRequest);

    String generateUsername (String firstName, String lastName);

    PaginationResponse<UserResponse> getAllUser (UserSearchDto userSearchDto);

    void firstChangePassword (FirstChangePasswordRequest firstChangePasswordRequest);
    void changePassword (ChangePasswordRequest changePasswordRequest);
}
