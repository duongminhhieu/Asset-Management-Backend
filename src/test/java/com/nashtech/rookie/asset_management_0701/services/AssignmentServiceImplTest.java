package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nashtech.rookie.asset_management_0701.dtos.filters.AssignmentFilter;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentUpdateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private AssignmentUpdateDto assignmentUpdateDto;
    private Asset asset;
    private Assignment assignment;
    private User user1;
    private User user2;
    private AssignmentFilter assignmentFilter;

    @BeforeEach
    void setUp () {

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
        assignmentUpdateDto = AssignmentUpdateDto.builder()
                .userId(2L)
                .assetId(1L)
                .build();
        assignmentFilter = AssignmentFilter.builder()
                .searchString("")
                .states(Set.of())
                .assignDate(LocalDate.of(2024, 9, 10))
                .orderBy("assetName")
                .sortDir("ASC")
                .pageSize(20)
                .pageNumber(1)
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

        @Test
        @WithMockUser(username = "User1", roles = "ADMIN")
        void testGetAllAssignment_validRequest_success (){
            // GIVEN
            when(authUtil.getCurrentUser()).thenReturn(user1);
            var pageRequest = PageRequest.of(0, 20, Sort.Direction.ASC, "asset_name");
            Page<Assignment> assignments = new PageImpl<Assignment>(List.of(assignment), pageRequest, 1L);
            given(assignmentRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(assignments);

            // WHEN
            var result = assignmentService.getAllAssignments(assignmentFilter);

            // THEN
            assertThat(result)
                .hasFieldOrPropertyWithValue("page", 1)
                .hasFieldOrPropertyWithValue("total", 1L)
                .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(result.getData()).hasSize(1);
        }

        @Test
        @WithMockUser(username = "User1", roles = "ADMIN")
        void testGetAllAssignment_nullFilterRequest_success (){
            // GIVEN
            assignmentFilter = AssignmentFilter.builder()
                .searchString(null)
                .states(null)
                .assignDate(null)
                .orderBy("assetName")
                .sortDir("ASC")
                .pageSize(20)
                .pageNumber(1)
                .build();
            when(authUtil.getCurrentUser()).thenReturn(user1);
            var pageRequest = PageRequest.of(0, 20, Sort.Direction.ASC, "asset_name");
            Page<Assignment> assignments = new PageImpl<Assignment>(List.of(assignment), pageRequest, 1L);
            given(assignmentRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(assignments);

            // WHEN
            var result = assignmentService.getAllAssignments(assignmentFilter);

            // THEN
            assertThat(result)
                .hasFieldOrPropertyWithValue("page", 1)
                .hasFieldOrPropertyWithValue("total", 1L)
                .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(result.getData()).hasSize(1);
        }

        @Test
        @WithMockUser(username = "User1", roles = "ADMIN")
        void updateAssignment_validRequest_returnAssignmentResponseDto() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
            when(assetRepository.save(any(Asset.class))).thenReturn(asset);

            // WHEN
            AssignmentResponseDto response = assignmentService.updateAssignment(1L, assignmentUpdateDto);

            // THEN
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
        }

        @Test
        void getAssignment_validRequest_returnAssignmentResponse() {
            // GIVEN
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

            // WHEN
            AssignmentResponse response = assignmentService.getAssignment(1L);

            // THEN
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
        }

        @Test
        @WithMockUser(username = "abc.com", roles = "ADMIN")
        void testGetMyAssignments_validRequest_returnPaginationAssignments() {
            // Given
            var pageRequest = PageRequest.of(0, 20);
            var resultPage = new PageImpl<>(List.of(assignment), pageRequest, 1);
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(assignmentRepository.findAllByAssignToIdAndAssignedDateLessThanEqual(any(), any(), any())).thenReturn(resultPage);

            // When
            PaginationResponse<AssignmentResponseDto> paginationResponse =  assignmentService.getMyAssignments(assignmentFilter);

            // Then
            assertThat(paginationResponse.getData().getFirst().getAssignedDate()).isEqualTo(assignment.getAssignedDate());
        }

        @Test
        @WithMockUser(username = "testUser")
        void testChangeState_validRequest_returnAssignmentStateAccepted() {
            // Given
            assignment.setState(EAssignmentState.WAITING);
            when(authUtil.getCurrentUserName()).thenReturn("testUser");
            when(assignmentRepository.findByIdAndAssignToUsername(1L, "testUser"))
                    .thenReturn(Optional.of(assignment));

            // When
            AssignmentResponseDto responseDto = assignmentService.changeState(1L, EAssignmentState.ACCEPTED);

            // Then
            verify(assignmentRepository).findByIdAndAssignToUsername(1L, "testUser");
            assertThat(responseDto.getState()).isEqualTo(EAssignmentState.ACCEPTED);
        }

        @Test
        @WithMockUser(username = "testUser")
        void testChangeState_validRequest_returnAssignmentStateDeclined() {
            // Given
            assignment.setState(EAssignmentState.WAITING);
            when(authUtil.getCurrentUserName()).thenReturn("testUser");
            when(assignmentRepository.findByIdAndAssignToUsername(1L, "testUser"))
                    .thenReturn(Optional.of(assignment));

            // When
            AssignmentResponseDto responseDto = assignmentService.changeState(1L, EAssignmentState.DECLINED);

            // Then
            verify(assignmentRepository).findByIdAndAssignToUsername(1L, "testUser");
            assertThat(responseDto.getState()).isEqualTo(EAssignmentState.DECLINED);
        }

        @Test
        @WithMockUser(username = "testUser")
        void testChangeState_validRequest_returnAssignmentStateWaitingDefault() {
            // Given
            when(authUtil.getCurrentUserName()).thenReturn("testUser");
            when(assignmentRepository.findByIdAndAssignToUsername(1L, "testUser"))
                    .thenReturn(Optional.of(assignment));

            // When
            AssignmentResponseDto responseDto = assignmentService.changeState(1L, EAssignmentState.WAITING);

            // Then
            verify(assignmentRepository).findByIdAndAssignToUsername(1L, "testUser");
            assertThat(responseDto.getState()).isEqualTo(EAssignmentState.WAITING);
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

        @Test
        @WithMockUser(username = "testUser")
        void testAssignmentNotFound() {
            when(authUtil.getCurrentUserName()).thenReturn("testUser");
            when(assignmentRepository.findByIdAndAssignToUsername(1L, "testUser"))
                    .thenReturn(Optional.empty());

            AppException exception = assertThrows(AppException.class, () -> {
                assignmentService.changeState(1L, EAssignmentState.ACCEPTED);
            });

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        @Test
        void updateAssignment_invalidAssignmentId_throwException() {
            // GIVEN
            when(assignmentRepository.findById(any())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        @Test
        void updateAssignment_invalidCurrentUser_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user2); // user2 is not the assigner

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGMENT_NOT_BELONG_TO_YOU);
        }

        @Test
        void updateAssignment_invalidAssignmentState_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.ACCEPTED); // State not WAITING
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGMENT_CANNOT_UPDATE);
        }

        @Test
        void updateAssignment_userNotFound_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(userRepository.findById(2L)).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
        }

        @Test
        void updateAssignment_assetNotFound_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
            when(assetRepository.findById(1L)).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_NOT_FOUND);
        }

        @Test
        void updateAssignment_assetNotAvailable_throwException() {
            // GIVEN
            Location location = Location
                    .builder()
                    .name("HCM")
                    .build();

            Asset asset1 = Asset.builder()
                    .id(2L)
                    .name("Asset1")
                    .state(EAssetState.ASSIGNED)
                    .installDate(LocalDate.now())
                    .specification("Specification")
                    .location(location)
                    .build();
            assignment.setState(EAssignmentState.WAITING);
            assignmentUpdateDto.setAssetId(asset1.getId());
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
            when(assetRepository.findById(2L)).thenReturn(Optional.of(asset1));

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_STATE_NOT_AVAILABLE);
        }

        @Test
        void updateAssignment_invalidAssetLocation_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            Location loc = Location.builder().name("Ha Noi").build();
            asset.setLocation(loc);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSET_LOCATION_INVALID_WITH_ADMIN);
        }

        @Test
        void updateAssignment_invalidUserLocation_throwException() {
            // GIVEN
            assignment.setState(EAssignmentState.WAITING);
            Location loc = Location.builder().name("Ha Noi").build();
            user2.setLocation(loc);
            when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
            when(authUtil.getCurrentUser()).thenReturn(user1);
            when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
            when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.updateAssignment(1L, assignmentUpdateDto);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_LOCATION_INVALID_WITH_ADMIN);
        }

        @Test
        void getAssignment_invalidAssignmentId_throwException() {
            // GIVEN
            when(assignmentRepository.findById(any())).thenReturn(Optional.empty());

            // WHEN
            var exception = assertThrows(AppException.class, () -> {
                assignmentService.getAssignment(1L);
            });

            // THEN
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.ASSIGNMENT_NOT_FOUND);
        }
    }
}
