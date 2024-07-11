package com.nashtech.rookie.asset_management_0701.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "invalid_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvalidToken extends AuditEntity<String> {
    @Id
    private String idToken;

    private Instant expiryDate;

    @ManyToOne
    private User user;

    @Override
    public String toString () {
        return idToken;
    }
}
