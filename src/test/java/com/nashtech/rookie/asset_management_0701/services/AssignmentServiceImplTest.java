package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.repositories.AssetRepository;
import com.nashtech.rookie.asset_management_0701.repositories.AssignmentRepository;
import com.nashtech.rookie.asset_management_0701.services.assignment.AssignmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AssignmentServiceImplTest {

    @MockBean
    private AssignmentRepository assignmentRepository;

    @MockBean
    private AssetRepository assetRepository;

    @Autowired
    private AssignmentServiceImpl assignmentService;

    private Asset asset;
    private Assignment assignment;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {

        user1 = User.builder()
                .id(1L)
                .username("User1")
                .firstName("User1")
                .lastName("User1")
                .build();

        user2 = User.builder()
                .id(2L)
                .username("User2")
                .firstName("User2")
                .lastName("User2")
                .build();

        asset = Asset.builder()
                .id(1L)
                .name("Asset1")
                .state(EAssetState.AVAILABLE)
                .installDate(LocalDate.now())
                .specification("Specification")
                .build();

        assignment = Assignment.builder()
                .id(1L)
                .asset(asset)
                .assignedDate(LocalDate.now())
                .assignTo(user2)
                .assignBy(user1)
                .state(EAssignmentState.ACCEPTED)
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        void getAssignmentHistory_validRequest_returnPaginationHistory() {
            // GIVEN
            var pageRequest = PageRequest.of(0, 20);
            var resultPage = new PageImpl<>(List.of(assignment), pageRequest, 1);
            when(assetRepository.findById(any())).thenReturn(Optional.of(asset));
            when(assignmentRepository.findAllByAsset(any(), any())).thenReturn(resultPage);

            // WHEN
            PaginationResponse<AssignmentHistory> paginationResponse =  assignmentService.getAssignmentHistory(1L, 1, 20);

            // THEN
            assertThat(paginationResponse.getData().getFirst().getAssignedDate()).isEqualTo(assignment.getAssignedDate());
        }
    }


    @Nested
    class UnhappyCase {

        @Test
        void getAssignmentHistory_invalidAssetId_throwException() {
            // GIVEN
            when(assetRepository.findById(any())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.getAssignmentHistory(1L, 1, 20);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_NOT_FOUND);
        }
    }
}
