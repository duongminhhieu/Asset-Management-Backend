package com.nashtech.rookie.asset_management_0701.services.returning_request;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.repositories.ReturningRequestRepository;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReturningRequestServiceImpl implements ReturningRequestService {

    private final ReturningRequestRepository returningRequestRepository;
    private final AuthUtil authUtil;
}
