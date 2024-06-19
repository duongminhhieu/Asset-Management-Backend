package com.nashtech.rookie.asset_management_0701.services.asset;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
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
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.utils.asset_utils.AssetUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AssetResponseDto createAsset (AssetCreateDto assetCreateDto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var category = categoryRepository
                .findByName(assetCreateDto.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Asset asset = assetMapper.dtoToEntity(assetCreateDto);
        asset.setCategory(category);
        asset.setLocation(user.getLocation());

        Long count = assetRepository.countByAssetCodeStartingWith(category.getCode()) + 1;
        asset.setAssetCode(AssetUtil.generateAssetCode(count, category.getCode()));

        asset = assetRepository.save(asset);
        return assetMapper.entityToDto(asset);
    }

    @Override
    public List<AssetResponseDto> getAllAssets () {
        return assetRepository.findAll().stream().map(assetMapper::entityToDto).toList();
    }
}
