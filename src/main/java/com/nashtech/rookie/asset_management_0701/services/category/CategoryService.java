package com.nashtech.rookie.asset_management_0701.services.category;

import java.util.List;

import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto createCategory (CategoryCreateDto dto);

    List<CategoryResponseDto> getAllCategories ();
}
