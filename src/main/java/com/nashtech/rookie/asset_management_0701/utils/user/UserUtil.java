package com.nashtech.rookie.asset_management_0701.utils.user;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;

public interface UserUtil {
    String generateUsername (UserRequest userRequest);

    String generateUsernameFromWeb (String firstName, String lastName);
}
