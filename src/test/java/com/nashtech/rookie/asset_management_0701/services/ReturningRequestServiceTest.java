package com.nashtech.rookie.asset_management_0701.services;

import com.nashtech.rookie.asset_management_0701.dtos.filters.ReturningRequestFilter;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import com.nashtech.rookie.asset_management_0701.repositories.ReturningRequestRepository;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestService;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReturningRequestServiceTest {

    @MockBean
    private ReturningRequestRepository returningRequestRepository;

    @MockBean
    private AuthUtil authUtil;

    @Autowired
    private ReturningRequestService returningRequestService;

    private User user1;
    private User user2;
    private Asset asset;
    private Assignment assignment;
    private ReturningRequest returningRequest;
    private ReturningRequestFilter returningRequestFilter;

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
        returningRequest = ReturningRequest.builder()
                .id(1L)
                .requestedBy(user1)
                .acceptedBy(user2)
                .returnDate(LocalDate.now())
                .assignment(assignment)
                .state(EAssignmentReturnState.WAITING_FOR_RETURNING)
                .build();
        returningRequestFilter = ReturningRequestFilter.builder()
                .searchString("")
                .states(Set.of())
                .orderBy("assetName")
                .sortDir("ASC")
                .pageSize(20)
                .pageNumber(1)
                .build();
    }

    @Nested
    class HappyCase {

        @Test
        @WithMockUser(username = "user1", roles = "ADMIN")
        void testGetAllReturningRequests_validRequest_success () {
            // GIVEN
            when(authUtil.getCurrentUser()).thenReturn(user1);
            var pageRequest = PageRequest.of(0, 20, Sort.Direction.ASC, "assetName");
            Page<ReturningRequest> returningRequests = new PageImpl<>(List.of(returningRequest), pageRequest, 1L);
            given(returningRequestRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(returningRequests);

            // WHEN
            var result = returningRequestService.getAllReturningRequests(returningRequestFilter);

            // THEN
            assertThat(result)
                    .hasFieldOrPropertyWithValue("page", 1)
                    .hasFieldOrPropertyWithValue("total", 1L)
                    .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(result.getData()).hasSize(1);
        }

        @Test
        @WithMockUser(username = "user1", roles = "ADMIN")
        void testGetAllReturningRequests_nullFilterRequest_success () {
            // GIVEN
            returningRequestFilter = ReturningRequestFilter.builder()
                    .searchString(null)
                    .states(null)
                    .orderBy("assetName")
                    .sortDir("ASC")
                    .pageSize(20)
                    .pageNumber(1)
                    .build();
            when(authUtil.getCurrentUser()).thenReturn(user1);
            var pageRequest = PageRequest.of(0, 20, Sort.Direction.ASC, "assetName");
            Page<ReturningRequest> returningRequests = new PageImpl<>(List.of(returningRequest), pageRequest, 1L);
            given(returningRequestRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(returningRequests);

            // WHEN
            var result = returningRequestService.getAllReturningRequests(returningRequestFilter);

            // THEN
            assertThat(result)
                    .hasFieldOrPropertyWithValue("page", 1)
                    .hasFieldOrPropertyWithValue("total", 1L)
                    .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(result.getData()).hasSize(1);
        }

        @Test
        @WithMockUser(username = "user1", roles = "ADMIN")
        void testGetAllReturningRequests_nullStateFilterRequest_success () {
            // GIVEN
            returningRequestFilter = ReturningRequestFilter.builder()
                    .searchString("car")
                    .states(null)
                    .orderBy("assetName")
                    .sortDir("ASC")
                    .pageSize(20)
                    .pageNumber(1)
                    .build();
            when(authUtil.getCurrentUser()).thenReturn(user1);
            var pageRequest = PageRequest.of(0, 20, Sort.Direction.ASC, "assetName");
            Page<ReturningRequest> returningRequests = new PageImpl<>(List.of(returningRequest), pageRequest, 1L);
            given(returningRequestRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(returningRequests);

            // WHEN
            var result = returningRequestService.getAllReturningRequests(returningRequestFilter);

            // THEN
            assertThat(result)
                    .hasFieldOrPropertyWithValue("page", 1)
                    .hasFieldOrPropertyWithValue("total", 1L)
                    .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(result.getData()).hasSize(1);
        }
    }
}
