package com.nashtech.rookie.asset_management_0701.dtos.responses.asset;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssetResponseDto {
    private String id;

    private String name;

    private String specification;

    private String assetCode;

    private LocalDate installDate;

    @Enumerated(EnumType.STRING)
    private EAssetState state;

    private LocationResponse location;

    private String category;

    private Long version;

}
