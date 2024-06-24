package com.nashtech.rookie.asset_management_0701.services.location;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.dtos.requests.location.LocationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.LocationMapper;
import com.nashtech.rookie.asset_management_0701.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService{
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationResponse> getAllLocation () {
        return locationMapper.toLocationResponses(locationRepository.findAll());
    }

    @Transactional
    @Override
    public LocationResponse createLocation (LocationRequest locationRequest) {
        if (locationRepository.existsByName(locationRequest.getName())) {
            throw new AppException(ErrorCode.LOCATION_NAME_ALREADY_EXISTED);
        }

        if (locationRepository.existsByCode(locationRequest.getCode())) {
            throw new AppException(ErrorCode.LOCATION_CODE_ALREADY_EXISTED);
        }

        Location location = locationMapper.toLocationEntity(locationRequest);

        return locationMapper.toLocationResponse(locationRepository.save(location));
    }
}
