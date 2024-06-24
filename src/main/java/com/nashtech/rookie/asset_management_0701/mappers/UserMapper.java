package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.entities.User;

@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "hashPassword", ignore = true)
    @Mapping(target = "staffCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "location", ignore = true)
    User toUser (UserRequest userRequest);

    @Mapping(source = "role", target = "type")
    @Mapping(source = "location", target = "location")
    UserResponse toUserResponse (User user);
}
