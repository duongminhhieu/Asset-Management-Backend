package com.nashtech.rookie.asset_management_0701.repositories;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nashtech.rookie.asset_management_0701.entities.InvalidToken;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {

    boolean existsByIdToken (String idToken);

    @Modifying
    @Query("delete from InvalidToken it where it.expiryDate < ?1")
    void deleteExpiredTokens (Instant now);
}
