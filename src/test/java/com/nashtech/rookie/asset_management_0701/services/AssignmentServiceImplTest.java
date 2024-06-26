package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.repositories.AssetRepository;
import com.nashtech.rookie.asset_management_0701.repositories.AssignmentRepository;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.services.assignment.AssignmentServiceImpl;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
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

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthUtil authUtil;

    @Autowired
    private AssignmentServiceImpl assignmentService;

    private AssignmentCreateDto assignmentCreateDto;
    private Asset asset;
    private Assignment assignment;
    private User user1;
    private User user2;

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

        user2 = User.builder()
                .id(2L)
                .username("User2")
                .firstName("User2")
                .lastName("User2")
                .location(location)
                .build();

        asset = Asset.builder()
                .id(1L)
                .name("Asset1")
                .state(EAssetState.AVAILABLE)
                .installDate(LocalDate.now())
                .specification("Specification")
                .location(location)
                .build();

        assignment = Assignment.builder()
                .id(1L)
                .asset(asset)
                .assignedDate(LocalDate.now())
                .assignTo(user2)
                .assignBy(user1)
                .state(EAssignmentState.ACCEPTED)
                .build();
        assignmentCreateDto = AssignmentCreateDto.builder()
                .userId(1L)
                .assetId(1L)
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

        @Test
        void createAssignment_validRequest_returnAssignmentResponseDto() {
            // GIVEN
            when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);
            when(assetRepository.save(any(Asset.class))).thenReturn(asset);

            // WHEN
            AssignmentResponseDto response = assignmentService.createAssignment(assignmentCreateDto);

            // THEN
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
        }

        @Test
        void testDeleteAssignment_validRequest_success() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            when(assignmentRepository.findById(any())).thenReturn(Optional.of(assignment));

            // WHEN
            assignmentService.deleteAssignment(1L);

            // THEN
            assertThat(asset.getState()).isEqualTo(EAssetState.AVAILABLE);
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

        @Test
        void createAssignment_invalidRequest_returnUserNotFoundException() {
            // GIVEN
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.createAssignment(assignmentCreateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
        }

        @Test
        void createAssignment_invalidRequest_returnAssetNotFoundException() {
            // GIVEN
            when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
            when(assetRepository.findById(1L)).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.createAssignment(assignmentCreateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_NOT_FOUND);
        }

        @Test
        void createAssignment_invalidRequest_returnAssetNotAvailableException() {
            // GIVEN
            asset.setState(EAssetState.ASSIGNED);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.createAssignment(assignmentCreateDto);
            });

            // THEN

            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_STATE_NOT_AVAILABLE);
        }

        @Test
        void createAssignment_invalidRequest_returnAssetLocationInvalidException() {
            // GIVEN
            Location loc = Location.builder()
                            .name("Ha Noi")
                            .build();
            asset.setLocation(loc);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
            when(authUtil.getCurrentUser()).thenReturn(user1);

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.createAssignment(assignmentCreateDto);
            });

            // THEN

            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_LOCATION_INVALID_WITH_ADMIN);
        }

        @Test
        void createAssignment_invalidRequest_returnUserLocationInvalidException() {
            // GIVEN
            Location loc = Location.builder()
                    .name("Ha Noi")
                    .build();
            user2.setLocation(loc);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user2));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
            when(authUtil.getCurrentUser()).thenReturn(user1);

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.createAssignment(assignmentCreateDto);
            });

            // THEN

            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_LOCATION_INVALID_WITH_ADMIN);
        }

        @Test
        void testDeleteAssignment_invalidAssignmentId_throwException() {
            // GIVEN
            when(assignmentRepository.findById(any())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.deleteAssignment(1L);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        @Test
        void testDeleteAssignment_invalidAssignmentState_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.ACCEPTED);
            when(assignmentRepository.findById(any())).thenReturn(Optional.of(assignment));

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.deleteAssignment(1L);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGNMENT_CANNOT_DELETE);
        }
    }
}
