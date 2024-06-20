package com.nashtech.rookie.asset_management_0701.services.asset;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.AssetMapper;
import com.nashtech.rookie.asset_management_0701.repositories.AssetRepository;
import com.nashtech.rookie.asset_management_0701.repositories.CategoryRepository;
import com.nashtech.rookie.asset_management_0701.utils.asset_utils.AssetUtil;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final CategoryRepository categoryRepository;
    private final AuthUtil authUtil;

    @Override
    @Transactional
    public AssetResponseDto createAsset (AssetCreateDto assetCreateDto) {
        var category = categoryRepository
                .findByName(assetCreateDto.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        validateInstallDate(assetCreateDto);
        Asset asset = assetMapper.toAsset(assetCreateDto);
        asset.setCategory(category);
        asset.setLocation(authUtil.getCurrentUser().getLocation());

        Long count = assetRepository.countByAssetCodeStartingWith(category.getCode()) + 1;
        asset.setAssetCode(AssetUtil.generateAssetCode(count, category.getCode()));

        asset = assetRepository.save(asset);
        return assetMapper.toAssetResponseDto(asset);
    }

    private void validateInstallDate (AssetCreateDto assetCreateDto) {
        LocalDateTime installDate = assetCreateDto.getInstallDate();
        LocalDateTime toThreeMonthsAgo = LocalDateTime.now().minusMonths(3);

        if (installDate.isBefore(toThreeMonthsAgo)) {
            throw new AppException(ErrorCode.ASSET_INSTALLED_DATE_TOO_OLD);
        }
    }

    @Override
    public List<AssetResponseDto> getAllAssets () {
        return assetRepository.findAll().stream().map(assetMapper::toAssetResponseDto).toList();
    }
}
