package com.nashtech.rookie.asset_management_0701.entities;

import java.time.LocalDateTime;

import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets")
public class Asset extends AuditEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specification;

    private String assetCode;

    private LocalDateTime installDate;

    @Enumerated(EnumType.STRING)
    private EAssetState state;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Category category;
}
