package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Asset;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    @Mapping(source = "category.name", target = "category")
    AssetResponseDto entityToDto (Asset asset);

    @Mapping(target = "category", ignore = true)
    Asset dtoToEntity (AssetCreateDto assetCreateDto);
}
