package com.nashtech.rookie.asset_management_0701.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.filters.AssetFilter;
import com.nashtech.rookie.asset_management_0701.dtos.requests.asset.AssetCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.PaginationResponse;
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
    public APIResponse<PaginationResponse<AssetResponseDto>> getAllAssets (@Valid @ModelAttribute AssetFilter filter) {
        return APIResponse.<PaginationResponse<AssetResponseDto>>builder()
                .result(assetService.getAllAssets(filter))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<AssetResponseDto> getAssetById (@PathVariable Long id) {
        return APIResponse.<AssetResponseDto>builder()
                .result(assetService.getAssetById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  APIResponse<String> deleteAsset (@PathVariable Long id) {
        assetService.deleteAsset(id);
        return APIResponse.<String>builder()
                .message("Delete asset successfully")
                .build();
    }

    @GetMapping("/exist-assignments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  APIResponse<Boolean> existAssignments (@PathVariable Long id) {
        return APIResponse.<Boolean>builder()
                .result(assetService.existAssignments(id))
                .build();
    }
}
