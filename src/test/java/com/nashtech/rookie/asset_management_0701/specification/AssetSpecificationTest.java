package com.nashtech.rookie.asset_management_0701.specification;

import static org.assertj.core.api.Assertions.assertThat;

import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.repositories.AssetRepository;
import com.nashtech.rookie.asset_management_0701.repositories.LocationRepository;
import com.nashtech.rookie.asset_management_0701.services.asset.AssetSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class AssetSpecificationTest {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private LocationRepository locationRepository;
    private Asset asset;

    @BeforeEach
    void setUp() {

        Location location = Location.builder()
                .name("Location1")
                .code("LC0001")
                .build();
        location = locationRepository.save(location);

        asset = Asset.builder()
                .state(EAssetState.AVAILABLE)
                .location(location)
                .assetCode("LP0001")
                .name("Asset1")
                .build();
        assetRepository.save(asset);
    }


    @Nested
    class HappyCase{
        @Test
        void hasAssetName_validName_returnSpecification() {
            // Given
            String assetName = "Asset1";
            // When
            var result = AssetSpecification.hasAssetName(assetName);
            
            // Then
            assertThat(result).isNotNull();

        }

        @Test
        void hasAssetCode_validCode_returnSpecification() {
            // Given
            String assetCode = "LP0001";
            // When
            var result = AssetSpecification.hasAssetCode(assetCode);
            // Then
            assertThat(result).isNotNull();
        }

        @Test
        void hasStates_validStates_returnSpecification() {
            // Given
            // When
            var result = AssetSpecification.hasStates(Set.of(EAssetState.AVAILABLE));
            // Then
            assertThat(result).isNotNull();
        }

        @Test
        void hasCategories_validCategories_returnSpecification() {
            // Given
            // When
            var result = AssetSpecification.hasCategories(List.of());
            // Then
            assertThat(result).isNotNull();
        }

        @Test
        void hasLocation_validLocation_returnSpecification() {
            // Given
            Location location = Location.builder()
                    .name("Location1")
                    .code("LC0001")
                    .id(1L)
                    .build();
            // When
            var result = AssetSpecification.hasLocation(location);
            // Then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    class UnHappyCase{
        @Test
        void hasAssetName_emptyAssetName_returnSpecification() {
            // Given
            String assetName = "";
            // When
            var result = AssetSpecification.hasAssetName(assetName);
            // Then
            assertThat(result).isNull();
        }

        @Test
        void hasAssetCode_emptyCode_returnSpecification() {
            // Given
            String assetCode = "";
            // When
            var result = AssetSpecification.hasAssetCode(assetCode);
            // Then
            assertThat(result).isNull();
        }

    }
}
