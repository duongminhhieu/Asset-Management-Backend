package com.nashtech.rookie.asset_management_0701.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;


@Repository
public interface ReturningRequestRepository extends JpaRepository<ReturningRequest, Long>
        , JpaSpecificationExecutor<ReturningRequest> {
    Optional<ReturningRequest> findByAssignmentId (Long id);
}
