package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.nashtech.rookie.asset_management_0701.dtos.filters.AssetFilter;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.nashtech.rookie.asset_management_0701.services.asset.AssetServiceImpl;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private AssetServiceImpl assetService;

    private AssetCreateDto assetCreateDto;
    private Asset asset;
    private AssetResponseDto assetResponseDto;
    private User user;
    private Category category;
    private AssetFilter assetFilter;

    @BeforeEach
    void setUp () {
        assetCreateDto = AssetCreateDto.builder()
                .name("Asset1")
                .category("Laptop")
                .specification("Specification")
                .installDate(LocalDate.now())
                .state(EAssetState.AVAILABLE)
                .build();

        category = Category.builder()
                .name("Laptop")
                .code("LP").build();

        asset = Asset.builder()
                .id(1L)
                .name("Asset1")
                .category(category)
                .specification("Specification")
                .installDate(LocalDate.now())
                .state(EAssetState.AVAILABLE)
                .assetCode("LP000001")
                .build();

        assetResponseDto = AssetResponseDto.builder()
                .name("Asset1")
                .category("Laptop")
                .specification("Specification")
                .installDate(LocalDate.now())
                .state(EAssetState.AVAILABLE)
                .assetCode("LP000001")
                .build();

        user = User.builder()
                .location(Location.builder()
                        .id(1L)
                        .name("Ha Noi")
                        .build())
                .build();


        assetFilter = AssetFilter.builder()
                .searchString(null)
                .states(Set.of(EAssetState.AVAILABLE))
                .categoryIds(Set.of(1L))
                .orderBy("name")
                .sortDir("ASC")
                .pageSize(20)
                .pageNumber(1)
                .build();
    }

    @Nested
    class HappyCase {
        @Test
        @WithMockUser(username = "username", roles = "ADMIN")
        void testAssetCreateDto_whenCreateAsset_returnCreateOk() {
            // Given
            given(authUtil.getCurrentUser()).willReturn(user);
            given(categoryRepository.findByName(anyString())).willReturn(Optional.of(category));
            given(assetMapper.toAsset(any(AssetCreateDto.class))).willReturn(asset);
            given(assetRepository.countByAssetCodeStartingWith(anyString())).willReturn(0L);
            given(assetRepository.save(any(Asset.class))).willReturn(asset);
            given(assetMapper.toAssetResponseDto(any(Asset.class))).willReturn(assetResponseDto);

            // When
            AssetResponseDto result = assetService.createAsset(assetCreateDto);

            // Then
            verify(authUtil).getCurrentUser();
            verify(categoryRepository).findByName("Laptop");
            verify(assetRepository).save(asset);
            assertEquals(assetResponseDto, result);
        }

        @Test
        @WithMockUser(username = "username", roles = "ADMIN")
        void testGetAllAssets_validRequest_returnPagination() {
            // Given
            given(authUtil.getCurrentUser()).willReturn(user);
            category.setId(1L);
            List<Category> categories = Collections.singletonList(category);
            given(categoryRepository.findAllById(assetFilter.getCategoryIds())).willReturn(categories);


            var pageRequest = PageRequest.of(0, 20, Sort.Direction.ASC, "name");
            Page<Asset> assets = new PageImpl<>(Collections.singletonList(asset), pageRequest, 1L);
            given(assetRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(assets);

            // When
            PaginationResponse<AssetResponseDto> actualResponse = assetService.getAllAssets(assetFilter);

            // Then
            assertThat(actualResponse)
                    .hasFieldOrPropertyWithValue("page", 1)
                    .hasFieldOrPropertyWithValue("total", 1L)
                    .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(actualResponse.getData()).hasSize(1);

        }

        @Test
        void testGetAssetById_validRequest_returnAsset() {
            // Given
            given(assetRepository.findById(1L)).willReturn(Optional.of(asset));
            given(assetMapper.toAssetResponseDto(asset)).willReturn(assetResponseDto);

            // When
            AssetResponseDto result = assetService.getAssetById(1L);

            // Then
            assertEquals(assetResponseDto, result);
        }
    }

    @Nested
    class UnHappyCase {
        @Test
        @WithMockUser(username = "username", roles = "ADMIN")
        void testAssetCreateDtoWithCategoryNotExisted_whenCreateAsset_returnException() {
            // Given
            given(categoryRepository.findByName(anyString())).willReturn(Optional.empty());

            // When
            assertThrows(AppException.class, () -> assetService.createAsset(assetCreateDto));

            // Then
            verify(categoryRepository).findByName("Laptop");
        }

        @Test
        @WithMockUser(username = "username", roles = "ADMIN")
        void testAssetCreateDtoWithInstallOld_whenCreateAsset_returnException() {
            // Given
            assetCreateDto.setInstallDate(LocalDate.now().minusMonths(4));

            given(authUtil.getCurrentUser()).willReturn(user);
            given(categoryRepository.findByName(anyString())).willReturn(Optional.of(category));

            // When
            assertThatThrownBy(() -> assetService.createAsset(assetCreateDto))
                    .isInstanceOf(AppException.class)
                    .hasMessageContaining(ErrorCode.ASSET_INSTALLED_DATE_TOO_OLD.getMessage());
        }

        @Test
        @WithMockUser(username = "username", roles = "ADMIN")
        void testGetAllAssets_invalidCategoryIds_returnException() {
            // Given
            given(authUtil.getCurrentUser()).willReturn(user);
            category.setId(1L);
            given(categoryRepository.findAllById(assetFilter.getCategoryIds())).willReturn(Collections.emptyList());

            // When
            AssertionsForClassTypes.assertThatThrownBy(() -> assetService.getAllAssets(assetFilter))
                    .isInstanceOf(AppException.class)
                    .hasMessageContaining(ErrorCode.CATEGORY_NOT_FOUND.getMessage());

            // Then
            verify(categoryRepository).findAllById(assetFilter.getCategoryIds());

        }

        @Test
        void testGetAssetById_invalidId_returnException() {
            // Given
            given(assetRepository.findById(1L)).willReturn(Optional.empty());

            // When
            assertThrows(AppException.class, () -> assetService.getAssetById(1L));

            // Then
            verify(assetRepository).findById(1L);
        }
    }
}
