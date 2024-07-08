package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetUpdateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    @Mapping(source = "category.name", target = "category")
    AssetResponseDto toAssetResponseDto (Asset asset);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "assetCode", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    Asset toAsset (AssetCreateDto assetCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "assetCode", ignore = true)
    @Mapping(target = "category", ignore = true)
    Asset updateAsset (@MappingTarget Asset asset, AssetUpdateDto assetUpdateDto);
}
