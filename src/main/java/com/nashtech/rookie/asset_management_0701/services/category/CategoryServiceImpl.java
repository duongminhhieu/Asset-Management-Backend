package com.nashtech.rookie.asset_management_0701.services.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Category;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.CategoryMapper;
import com.nashtech.rookie.asset_management_0701.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto createCategory (CategoryCreateDto categoryCreateDto) {
        categoryRepository.findByName(categoryCreateDto.getName()).ifPresent(c -> {
            throw new AppException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTED);
        });

        categoryRepository.findByCode(categoryCreateDto.getCode()).ifPresent(c -> {
            throw new AppException(ErrorCode.CATEGORY_PREFIX_ALREADY_EXISTED);
        });

        Category category = categoryMapper.toCategory(categoryCreateDto);
        category.setCountAmount(0L);
        categoryRepository.save(category);

        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories () {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponseDto)
                .toList();
    }
}
