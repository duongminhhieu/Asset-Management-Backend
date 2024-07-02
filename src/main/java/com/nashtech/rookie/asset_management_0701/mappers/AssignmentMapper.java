package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.requests.assignment.AssignmentUpdateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentHistory;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.assigment.AssignmentResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Assignment;

@Mapper(componentModel = "spring", uses = {AssetMapper.class, UserMapper.class})
public interface AssignmentMapper {

    @Mapping(source = "assignTo.username", target = "assignTo")
    @Mapping(source = "assignBy.username", target = "assignBy")
    @Mapping(source = "returningRequest.returnDate", target = "returnDate")
    AssignmentHistory toAssignmentHistory (Assignment assignment);

    @Mapping(target = "assignTo", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignBy", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "returningRequest", ignore = true)
    Assignment toAssignment (AssignmentCreateDto assignmentCreateDto);

    @Mapping(source = "assignTo.username", target = "assignTo")
    @Mapping(source = "assignBy.username", target = "assignBy")
    @Mapping(source = "returningRequest.returnDate", target = "returnDate")
    @Mapping(source = "returningRequest.id", target = "returningRequestId")
    AssignmentResponseDto toAssignmentResponseDto (Assignment assignment);

    @Mapping(source = "returningRequest.returnDate", target = "returnDate")
    AssignmentResponse toAssignmentResponse (Assignment assignment);

    @Mapping(target = "assignTo", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignBy", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "returningRequest", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Assignment updateEntity (@MappingTarget Assignment entity, AssignmentUpdateDto dto);
}
