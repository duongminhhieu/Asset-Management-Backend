package com.nashtech.rookie.asset_management_0701.utils.auth_util;

import com.nashtech.rookie.asset_management_0701.entities.User;

public interface AuthUtil{
    String getCurrentUserName ();
    User getCurrentUser ();
}
