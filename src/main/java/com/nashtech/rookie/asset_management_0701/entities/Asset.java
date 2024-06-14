package com.nashtech.rookie.asset_management_0701.entities;

import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets")
public class Asset extends AuditEntity<String>{

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
