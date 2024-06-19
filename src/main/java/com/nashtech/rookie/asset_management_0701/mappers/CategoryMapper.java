package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;

import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto entityToDto (Category category);

    Category dtoToEntity (CategoryCreateDto categoryCreateDto);
}
