package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.entities.Category;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.mappers.AssetMapper;
import com.nashtech.rookie.asset_management_0701.repositories.AssetRepository;
import com.nashtech.rookie.asset_management_0701.repositories.CategoryRepository;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.services.asset.AssetServiceImpl;

@SpringBootTest
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AssetServiceImpl assetService;

    private AssetCreateDto assetCreateDto;
    private Asset asset;
    private AssetResponseDto assetResponseDto;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        assetCreateDto = AssetCreateDto.builder()
                .name("Asset1")
                .category("Laptop")
                .specification("Specification")
                .installDate(LocalDateTime.now())
                .state(EAssetState.AVAILABLE)
                .build();

        asset = Asset.builder()
                .name("Asset1")
                .category(category)
                .specification("Specification")
                .installDate(LocalDateTime.now())
                .state(EAssetState.AVAILABLE)
                .assetCode("LP000001")
                .build();

        assetResponseDto = AssetResponseDto.builder()
                .name("Asset1")
                .category("Laptop")
                .specification("Specification")
                .installDate(LocalDateTime.now())
                .state(EAssetState.AVAILABLE)
                .assetCode("LP000001")
                .build();

        user = User.builder()
                .location(Location.builder().id(1L).name("Ha Noi").build())
                .build();

        category = Category.builder().name("Laptop").code("LP").build();
    }

    @Nested
    class HappyCase {
        @Test
        void givenAssetCreateDto_whenCreateAsset_returnCreateOk() {
            // Mock SecurityContextHolder
            SecurityContextHolder.setContext(securityContext);
            given(securityContext.getAuthentication()).willReturn(authentication);
            given(authentication.getName()).willReturn("username");

            // Given
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
            given(categoryRepository.findByName(anyString())).willReturn(Optional.of(category));
            given(assetMapper.dtoToEntity(any(AssetCreateDto.class))).willReturn(asset);
            given(assetRepository.countByAssetCodeStartingWith(anyString())).willReturn(0L);
            given(assetRepository.save(any(Asset.class))).willReturn(asset);
            given(assetMapper.entityToDto(any(Asset.class))).willReturn(assetResponseDto);

            // When
            AssetResponseDto result = assetService.createAsset(assetCreateDto);

            // Then
            verify(userRepository).findByUsername("username");
            verify(categoryRepository).findByName("Laptop");
            verify(assetRepository).save(asset);
            assertEquals(assetResponseDto, result);
        }

        @Test
        void whenGetAllAssets_returnAllAssets() {
            // Given
            List<Asset> assets = Collections.singletonList(asset);
            List<AssetResponseDto> assetResponseDtoList = Collections.singletonList(assetResponseDto);
            given(assetRepository.findAll()).willReturn(assets);
            given(assetMapper.entityToDto(asset)).willReturn(assetResponseDto);

            // When
            List<AssetResponseDto> result = assetService.getAllAssets();

            // Then
            verify(assetRepository).findAll();
            assertThat(result).isEqualTo(assetResponseDtoList);
        }
    }

    @Nested
    class UnHappyCase {

        @Test
        void givenAssetCreateDtoWithUserNotFound_whenCreateAsset_returnException() {
            // Mock SecurityContextHolder
            SecurityContextHolder.setContext(securityContext);
            given(securityContext.getAuthentication()).willReturn(authentication);
            given(authentication.getName()).willReturn("username");

            // Given
            given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

            // When
            assertThrows(AppException.class, () -> {
                assetService.createAsset(assetCreateDto);
            });

            // Then
            verify(userRepository).findByUsername("username");
        }

        @Test
        void givenAssetCreateDtoWithCategoryNotExisted_whenCreateAsset_returnException() {
            // Mock SecurityContextHolder
            SecurityContextHolder.setContext(securityContext);
            given(securityContext.getAuthentication()).willReturn(authentication);
            given(authentication.getName()).willReturn("username");

            // Given
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
            given(categoryRepository.findByName(anyString())).willReturn(Optional.empty());

            // When
            assertThrows(AppException.class, () -> {
                assetService.createAsset(assetCreateDto);
            });

            // Then
            verify(userRepository).findByUsername("username");
            verify(categoryRepository).findByName("Laptop");
        }
    }
}
