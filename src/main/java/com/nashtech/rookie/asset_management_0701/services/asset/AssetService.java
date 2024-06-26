package com.nashtech.rookie.asset_management_0701.services.asset;

import com.nashtech.rookie.asset_management_0701.dtos.filters.AssetFilter;
import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;

public interface AssetService {
    AssetResponseDto createAsset (AssetCreateDto assetCreateDto);

    PaginationResponse<AssetResponseDto> getAllAssets (AssetFilter assetFilter);

    AssetResponseDto getAssetById (Long id);

    void deleteAsset (Long id);
}
