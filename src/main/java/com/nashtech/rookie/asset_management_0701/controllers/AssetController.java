package com.nashtech.rookie.asset_management_0701.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.asset.AssetResponseDto;
import com.nashtech.rookie.asset_management_0701.services.asset.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<AssetResponseDto> createAsset (@Valid @RequestBody AssetCreateDto assetCreateDto) {
        return APIResponse.<AssetResponseDto>builder()
                .result(assetService.createAsset(assetCreateDto))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<List<AssetResponseDto>> getAllAssets () {
        return APIResponse.<List<AssetResponseDto>>builder()
                .result(assetService.getAllAssets())
                .build();
    }
}
