package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;

@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface UserMapper {
    @Mapping(source = "locationId", target = "location", qualifiedByName = "idToLocation")
    User toUser (UserRequest userRequest);

    @Mapping(source = "role", target = "type")
    @Mapping(source = "location", target = "location")
    UserResponse toUserResponse (User user);

    @Named("idToLocation")
    default Location idToLocation (Long locationId) {
        if (locationId == null) {
            throw new AppException(ErrorCode.ADMIN_NULL_LOCATION);
        }
        Location location = new Location();
        location.setId(locationId);
        return location;
    }
}
