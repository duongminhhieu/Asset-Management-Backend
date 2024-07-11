package com.nashtech.rookie.asset_management_0701.services.auth;

import com.nashtech.rookie.asset_management_0701.dtos.requests.auth.AuthenticationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.auth.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login (AuthenticationRequest request);

    void logout (String token);
}
