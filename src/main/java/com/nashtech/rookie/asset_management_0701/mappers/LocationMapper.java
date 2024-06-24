package com.nashtech.rookie.asset_management_0701.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nashtech.rookie.asset_management_0701.dtos.requests.location.LocationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.entities.Location;


@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Location toLocationEntity (LocationRequest locationRequest);

    LocationResponse toLocationResponse (Location location);

    List<LocationResponse> toLocationResponses (List<Location> locations);
}
