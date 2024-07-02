package com.nashtech.rookie.asset_management_0701.services.returning_request;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.repositories.ReturningRequestRepository;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReturningRequestServiceImpl implements ReturningRequestService {

    private final ReturningRequestRepository returningRequestRepository;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public void completeReturningRequest (Long id) {

        ReturningRequest returningRequest = returningRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RETURNING_REQUEST_NOT_FOUND));

        if (returningRequest.getState() != EAssignmentReturnState.WAITING_FOR_RETURNING) {
            throw new AppException(ErrorCode.RETURNING_REQUEST_STATE_INVALID);
        }

        returningRequest.setState(EAssignmentReturnState.COMPLETED);
        returningRequest.getAssignment().getAsset().setState(EAssetState.AVAILABLE);
        returningRequest.getAssignment().setState(EAssignmentState.RETURNED);
        returningRequest.setReturnDate(LocalDate.now());
        returningRequest.setAcceptedBy(authUtil.getCurrentUser());
        returningRequestRepository.save(returningRequest);
    }

    @Override
    @Transactional
    public void cancelReturningRequest (Long id) {
        ReturningRequest returningRequest = returningRequestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RETURNING_REQUEST_NOT_FOUND));

        if (returningRequest.getState() != EAssignmentReturnState.WAITING_FOR_RETURNING) {
            throw new AppException(ErrorCode.RETURNING_REQUEST_STATE_INVALID);
        }

        returningRequestRepository.deleteById(id);
    }
}
