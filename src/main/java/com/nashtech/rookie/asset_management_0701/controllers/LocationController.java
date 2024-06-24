package com.nashtech.rookie.asset_management_0701.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.location.LocationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.services.location.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<List<LocationResponse>> getAllLocations () {
        var result = locationService.getAllLocation();
        return APIResponse.<List<LocationResponse>>builder()
                .result(result)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<LocationResponse> createLocation (@RequestBody @Valid LocationRequest locationRequest) {
        LocationResponse locationResponse = locationService.createLocation(locationRequest);
        return APIResponse.<LocationResponse>builder().result(locationResponse).build();
    }
}
