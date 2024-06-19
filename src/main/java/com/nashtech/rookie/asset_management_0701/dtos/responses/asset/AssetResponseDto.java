package com.nashtech.rookie.asset_management_0701.dtos.responses.asset;

import java.time.LocalDateTime;

import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssetResponseDto {

    private String name;
    private String specification;
    private String assetCode;
    private LocalDateTime installDate;

    @Enumerated(EnumType.STRING)
    private EAssetState state;

    private Location location;
    private String category;
}
