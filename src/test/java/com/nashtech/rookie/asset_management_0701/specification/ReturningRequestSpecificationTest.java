package com.nashtech.rookie.asset_management_0701.specification;

import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;
import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import com.nashtech.rookie.asset_management_0701.services.returning_request.ReturningRequestSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReturningRequestSpecificationTest {

    @Mock
    private Root<ReturningRequest> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> path;

    @Mock
    private Join<Object, Object> join;

    @Mock
    private Predicate predicate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHasReturnDate() {
        LocalDate returnDate = LocalDate.now();
        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasReturnDate(returnDate);

        when(root.get("returnDate")).thenReturn(path);
        when(criteriaBuilder.equal(path, returnDate)).thenReturn(mock(Predicate.class));

        predicate = spec.toPredicate(root, query, criteriaBuilder);

        verify(root).get("returnDate");
        verify(criteriaBuilder).equal(path, returnDate);
        assertNotNull(predicate);
    }

    @Test
    void testHasReturnDateWithNull() {
        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasReturnDate(null);

        predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertNull(predicate);
    }

    @Test
    void testHasLocation() {
        Location location = new Location();
        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasLocation(location);

        when(root.join("requestedBy")).thenReturn(join);
        when(join.get("location")).thenReturn(path);
        when(criteriaBuilder.equal(path, location)).thenReturn(mock(Predicate.class));

        predicate = spec.toPredicate(root, query, criteriaBuilder);

        verify(root).join("requestedBy");
        verify(join).get("location");
        verify(criteriaBuilder).equal(path, location);
        assertNotNull(predicate);
    }

    @Test
    void testHasLocationWithNull() {
        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasLocation(null);

        predicate = spec.toPredicate(root, query, criteriaBuilder);

        assertNull(predicate);
    }

    @Test
    void testHasState() {
        Set<EAssignmentReturnState> states = new HashSet<>();
        states.add(EAssignmentReturnState.WAITING_FOR_RETURNING);
        states.add(EAssignmentReturnState.COMPLETED);

        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasState(states);

        when(root.get("state")).thenReturn(path);
        when(path.in(states)).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        verify(root).get("state");
        verify(path).in(states);
        assertNotNull(result);
    }

    @Test
    void testHasStateWithEmptySet() {
        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasState(Collections.emptySet());

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNull(result);
    }

    @Test
    void testHasStateWithNull() {
        Specification<ReturningRequest> spec = ReturningRequestSpecification.hasState(null);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNull(result);
    }
}





