package com.nashtech.rookie.asset_management_0701.services.location;

import java.util.List;

import com.nashtech.rookie.asset_management_0701.dtos.requests.location.LocationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;


public interface LocationService {
    List<LocationResponse> getAllLocation ();

    LocationResponse createLocation (LocationRequest locationRequest);
}
