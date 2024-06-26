package com.nashtech.rookie.asset_management_0701.services.assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.AssignmentMapper;
import com.nashtech.rookie.asset_management_0701.repositories.AssetRepository;
import com.nashtech.rookie.asset_management_0701.repositories.AssignmentRepository;
import com.nashtech.rookie.asset_management_0701.utils.PageSortUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentServiceImpl implements AssignmentService {

    private final AssetRepository assetRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;

    @Override
    public PaginationResponse<AssignmentHistory> getAssignmentHistory (Long assetId, Integer page, Integer size) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));

        Sort sort = Sort.by(Sort.Direction.DESC, "assignedDate");
        Pageable pageable = PageSortUtil.createPageRequest(page, size, sort);

        Page<Assignment> assignments = assignmentRepository.findAllByAsset(asset, pageable);

        return PaginationResponse.<AssignmentHistory>builder()
                .page(pageable.getPageNumber() + 1)
                .total(assignments.getTotalElements())
                .itemsPerPage(pageable.getPageSize())
                .data(assignments.map(assignmentMapper::toAssignmentHistory).toList())
                .build();
    }

    @Override
    @Transactional
    public void deleteAssignment (Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        if (assignment.getState() == EAssignmentState.ACCEPTED) {
            throw new AppException(ErrorCode.ASSIGNMENT_CANNOT_DELETE);
        }
        Asset asset = assignment.getAsset();
        asset.setState(EAssetState.AVAILABLE);
        assetRepository.save(asset);
        assignmentRepository.delete(assignment);
    }
}
