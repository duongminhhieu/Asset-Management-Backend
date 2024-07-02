package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;

import com.nashtech.rookie.asset_management_0701.dtos.responses.returning_request.ReturningRequestResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.ReturningRequest;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AssetMapper.class, AssignmentMapper.class})
public interface ReturningRequestMapper {

    ReturningRequestResponseDto toReturningRequestDto (ReturningRequest returningRequest);
}
