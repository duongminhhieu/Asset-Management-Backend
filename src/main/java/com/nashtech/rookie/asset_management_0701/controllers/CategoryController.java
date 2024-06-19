package com.nashtech.rookie.asset_management_0701.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.APIResponse;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;
import com.nashtech.rookie.asset_management_0701.services.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<CategoryResponseDto> createCategory (@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        return APIResponse.<CategoryResponseDto>builder()
                .result(categoryService.createCategory(categoryCreateDto))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public APIResponse<List<CategoryResponseDto>> getAllCategories () {
        return APIResponse.<List<CategoryResponseDto>>builder()
                .result(categoryService.getAllCategories())
                .build();
    }
}
