package com.nashtech.rookie.asset_management_0701.services;

import com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request.ReturningRequestResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.repositories.AssignmentRepository;
import com.nashtech.rookie.asset_management_0701.repositories.ReturningRequestRepository;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestServiceImpl;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import org.assertj.core.api.Assertions;
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
    private AuthUtil authUtil;

    @MockBean
    ReturningRequestRepository returningRequestRepository;

    @MockBean
    private AssignmentRepository assignmentRepository;

    @Autowired
    ReturningRequestServiceImpl returningRequestService;

    private ReturningRequest returningRequest;
    private Assignment assignment;
    private Assignment assignmentError;
    private Asset asset;
    private User user;
    private User user1;

    @BeforeEach
    void setUp() {
        Location location = Location
                .builder()
                .name("HCM")
                .build();

        user1 = User.builder()
                .id(1L)
                .username("User1")
                .firstName("User1")
                .lastName("User1")
                .location(location)
                .build();

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

        assignmentError = Assignment.builder()
                .id(2L)
                .asset(Asset.builder()
                        .id(1L)
                        .name("Asset1")
                        .state(EAssetState.AVAILABLE)
                        .installDate(LocalDate.now())
                        .specification("Specification")
                        .location(location)
                        .build())
                .assignedDate(LocalDate.now())
                .assignTo(user1)
                .assignBy(User.builder()
                        .id(2L)
                        .username("User2")
                        .firstName("User2")
                        .lastName("User2")
                        .location(location)
                        .build())
                .state(EAssignmentState.DECLINED)
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

        @Test
        void createReturningRequest_validRequest_returnReturningRequestDto() {
            // Given
            when(assignmentRepository.findByIdAndAssignToUsername(1L, "User1")).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(authUtil.getCurrentUserName()).thenReturn(user1.getUsername());
            when(returningRequestRepository.save(any())).thenReturn(returningRequest);

            // When
            ReturningRequestResponseDto result = returningRequestService.createReturningRequest(1L);

            // Then
            Assertions.assertThat(result.getId()).isEqualTo(returningRequest.getId());
            Assertions.assertThat(result.getState()).isEqualTo(EAssignmentReturnState.WAITING_FOR_RETURNING);
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


        @Test
        void createReturningRequest_invalidRequest_throwException() {
            // Given
            when(assignmentRepository.findByIdAndAssignToUsername(1L,"Duy")).thenReturn(Optional.empty());
            // When
            var exception = assertThrows(AppException.class, () -> {
                returningRequestService.createReturningRequest(1L);
            });
            // Then
            Assertions.assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGMENT_NOT_BELONG_TO_YOU);
        }

        @Test
        void createReturningRequest_assignmentStateNotAccepted_throwException() {
            // Given
            when(assignmentRepository.findByIdAndAssignToUsername(1L,"User1")).thenReturn(Optional.of(assignmentError));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(authUtil.getCurrentUserName()).thenReturn(user1.getUsername());
            // When
            var exception = assertThrows(AppException.class, () ->
                    returningRequestService.createReturningRequest(1L)
            );
            // Then
            Assertions.assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.RETURNING_REQUEST_STATE_INVALID);
        }

        @Test
        void createReturningRequest_assignmentAlreadyHasReturningRequest_throwException() {
            // Given
            assignment.setReturningRequest(returningRequest);
            when(assignmentRepository.findByIdAndAssignToUsername(1L,"User1")).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(authUtil.getCurrentUserName()).thenReturn(user1.getUsername());
            // When
            var exception = assertThrows(AppException.class, () ->
                    returningRequestService.createReturningRequest(1L)
            );
            // Then
            Assertions.assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.RETURNING_REQUEST_ALREADY_EXISTS);
        }
    }

}
