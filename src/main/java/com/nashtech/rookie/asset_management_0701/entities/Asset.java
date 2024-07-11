package com.nashtech.rookie.asset_management_0701.entities;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table(name = "assets", indexes = {@Index(name = "idx_asset_category_id", columnList = "category_id")})
public class Asset extends AuditEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(length = 1024)
    private String specification;

    @Column(length = 8)
    private String assetCode;

    private LocalDate installDate;

    @Enumerated(EnumType.STRING)
    private EAssetState state;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "asset")
    private Set<Assignment> assignments;

    @Version
    @ColumnDefault("0")
    private Long version;
}
