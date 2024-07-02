package com.nashtech.rookie.asset_management_0701.services;

import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.repositories.ReturningRequestRepository;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestServiceImpl;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReturningRequestServiceImplTest {


    @MockBean
    ReturningRequestRepository returningRequestRepository;

    @MockBean
    AuthUtil authUtil;

    @Autowired
    ReturningRequestServiceImpl returningRequestService;

    private ReturningRequest returningRequest;
    private Assignment assignment;
    private Asset asset;
    private User user;

    @BeforeEach
    void setUp() {


        asset = Asset.builder()
                .id(1L)
                .state(EAssetState.ASSIGNED)
                .assetCode("A0001")
                .build();

        assignment = Assignment.builder()
                .state(EAssignmentState.ACCEPTED)
                .assignedDate(LocalDate.now())
                .asset(asset)
                .build();

        returningRequest = ReturningRequest.builder()
                .id(1L)
                .assignment(assignment)
                .state(EAssignmentReturnState.WAITING_FOR_RETURNING)
                .build();

        user = User.builder()
                .id(1L)
                .username("admin")
                .role(ERole.ADMIN)
                .build();
    }


    @Nested
    class HappyCase {

        @Test
        void cancelReturningRequest_validRequest_success() {
            // Given
            Long id = 1L;
            when(returningRequestRepository.findById(any())).thenReturn(Optional.of(returningRequest));

            // When
            returningRequestService.cancelReturningRequest(id);
            // Then
            verify(returningRequestRepository, times(1)).deleteById(id);
        }

        @Test
        void completeReturningRequest_validRequest_success() {
            // Given
            Long id = 1L;
            when(returningRequestRepository.findById(any())).thenReturn(Optional.of(returningRequest));
            when(authUtil.getCurrentUser()).thenReturn(user);
            // When
            returningRequestService.completeReturningRequest(id);
            // Then
            assertThat(returningRequest.getState()).isEqualTo(EAssignmentReturnState.COMPLETED);
            assertThat(returningRequest.getAssignment().getAsset().getState()).isEqualTo(EAssetState.AVAILABLE);
            assertThat(returningRequest.getAssignment().getState()).isEqualTo(EAssignmentState.RETURNED);
            assertThat(returningRequest.getReturnDate()).isEqualTo(LocalDate.now());
        }
    }

    @Nested
    class UnhappyCase {

        @Test
        void cancelReturningRequest_invalidRequest_throwException() {
            // Given
            Long id = 1L;
            when(returningRequestRepository.findById(any())).thenReturn(Optional.empty());

            // When
            var exception =assertThrows(AppException.class, () -> {
                returningRequestService.cancelReturningRequest(id);
            });
            // Then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RETURNING_REQUEST_NOT_FOUND);
        }

        @Test
        void cancelReturningRequest_invalidState_throwException() {
            // Given
            Long id = 1L;
            returningRequest.setState(EAssignmentReturnState.COMPLETED);
            when(returningRequestRepository.findById(any())).thenReturn(Optional.of(returningRequest));

            // When
            var exception =assertThrows(AppException.class, () -> {
                returningRequestService.cancelReturningRequest(id);
            });
            // Then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RETURNING_REQUEST_STATE_INVALID);
        }

        @Test
        void completeReturningRequest_invalidRequest_throwException() {
            // Given
            Long id = 1L;
            when(returningRequestRepository.findById(any())).thenReturn(Optional.empty());

            // When
            var exception =assertThrows(AppException.class, () -> {
                returningRequestService.completeReturningRequest(id);
            });
            // Then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RETURNING_REQUEST_NOT_FOUND);
        }

        @Test
        void completeReturningRequest_invalidState_throwException() {
            // Given
            Long id = 1L;
            returningRequest.setState(EAssignmentReturnState.COMPLETED);
            when(returningRequestRepository.findById(any())).thenReturn(Optional.of(returningRequest));

            // When
            var exception =assertThrows(AppException.class, () -> {
                returningRequestService.completeReturningRequest(id);
            });
            // Then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RETURNING_REQUEST_STATE_INVALID);
        }

    }

}
