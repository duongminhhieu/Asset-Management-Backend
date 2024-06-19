package com.nashtech.rookie.asset_management_0701.services.asset;

import java.util.List;

import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;

public interface AssetService {
    AssetResponseDto createAsset (AssetCreateDto assetCreateDto);

    List<AssetResponseDto> getAllAssets ();
}
