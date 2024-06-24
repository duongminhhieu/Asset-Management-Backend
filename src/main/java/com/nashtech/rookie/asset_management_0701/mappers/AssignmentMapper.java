package com.nashtech.rookie.asset_management_0701.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    @Mapping(source = "assignTo.username", target = "assignTo")
    @Mapping(source = "assignBy.username", target = "assignBy")
    @Mapping(source = "returningRequest.returnDate", target = "returnDate")
    AssignmentHistory toAssignmentHistory (Assignment assignment);
}
