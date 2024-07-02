package com.nashtech.rookie.asset_management_0701.services.returning_request;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.dtos.filters.ReturningRequestFilter;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request.ReturningRequestResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.ReturningRequestMapper;
import com.nashtech.rookie.asset_management_0701.repositories.ReturningRequestRepository;
import com.nashtech.rookie.asset_management_0701.utils.PageSortUtil;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReturningRequestServiceImpl implements ReturningRequestService {
    private final AuthUtil authUtil;
    private final ReturningRequestRepository returningRequestRepository;
    private final ReturningRequestMapper returningRequestMapper;
    private final Map<String, String> sortBy = Map.ofEntries(
            Map.entry("assetName", "assignment_asset_name"),
            Map.entry("assetCode", "assignment_asset_assetCode"),
            Map.entry("requestedBy", "requestedBy_username"),
            Map.entry("acceptedBy", "acceptedBy_username"),
            Map.entry("assignedDate", "assignment_assignedDate"),
            Map.entry("state", "state"),
            Map.entry("id", "id"),
            Map.entry("returnDate", "returnDate")
    );

    @Override
    public PaginationResponse<ReturningRequestResponseDto> getAllReturningRequests (ReturningRequestFilter filter) {
        Sort sort = Sort.by(PageSortUtil.parseSortDirection(filter.getSortDir()), sortBy.get(filter.getOrderBy()));
        Pageable pageable = PageSortUtil.createPageRequest(filter.getPageNumber()
                , filter.getPageSize(), sort);
        Location currentLocation = authUtil.getCurrentUser().getLocation();

        Page<ReturningRequest> returningRequests = returningRequestRepository.findAll(
                Specification.where(ReturningRequestSpecification.hasAssetName(filter.getSearchString())
                                .or(ReturningRequestSpecification.hasAssetCode(filter.getSearchString()))
                                .or(ReturningRequestSpecification.hasRequestUserName(filter.getSearchString()))
                                .and(ReturningRequestSpecification.hasReturnDate(filter.getReturnDate()))
                                .and(ReturningRequestSpecification.hasLocation(currentLocation))
                                .and(ReturningRequestSpecification.hasState(filter.getStates())))
                        , pageable);

        return PaginationResponse.<ReturningRequestResponseDto>builder()
                .page(pageable.getPageNumber() + 1)
                .total(returningRequests.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(returningRequests.map(returningRequestMapper::toReturningRequestDto).toList())
                .build();
    }

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
