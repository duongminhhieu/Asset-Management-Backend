package com.nashtech.rookie.asset_management_0701.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDto toCategoryResponseDto (Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assets", ignore = true)
    Category toCategory (CategoryCreateDto categoryCreateDto);
}
