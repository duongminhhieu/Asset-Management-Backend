package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;

import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationResponse toLocationResponse (LocationResponse locationResponse);
}
