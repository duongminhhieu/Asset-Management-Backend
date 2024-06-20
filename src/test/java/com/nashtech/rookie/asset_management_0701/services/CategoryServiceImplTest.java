package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.nashtech.rookie.asset_management_0701.dtos.requests.category.CategoryCreateDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.category.CategoryResponseDto;
import com.nashtech.rookie.asset_management_0701.entities.Category;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.CategoryMapper;
import com.nashtech.rookie.asset_management_0701.repositories.CategoryRepository;
import com.nashtech.rookie.asset_management_0701.services.category.CategoryServiceImpl;

@SpringBootTest
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryCreateDto categoryCreateDto;
    private Category category;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        categoryCreateDto =
                CategoryCreateDto.builder().name("Category1").code("C1").build();

        category = Category.builder().name("Category1").code("C1").build();

        categoryResponseDto =
                CategoryResponseDto.builder().name("Category1").code("C1").build();
    }

    @Nested
    class HappyCase {
        @Test
        void testCreateNewCategory_thenCreateCategory_returnCreateOk() {
            // when
            when(categoryRepository.findByName(categoryCreateDto.getName())).thenReturn(Optional.empty());
            when(categoryRepository.findByCode(categoryCreateDto.getCode())).thenReturn(Optional.empty());
            when(categoryMapper.toCategory(categoryCreateDto)).thenReturn(category);
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toCategoryResponseDto(category)).thenReturn(categoryResponseDto);

            // then
            CategoryResponseDto result = categoryService.createCategory(categoryCreateDto);

            // return
            assertThat(result).isEqualTo(categoryResponseDto);
        }

        @Test
        void testGetAllCategories_thenGetCategories_returnAllCategories() {
            // when
            List<Category> categories = Collections.singletonList(category);
            when(categoryRepository.findAll()).thenReturn(categories);
            when(categoryMapper.toCategoryResponseDto(category)).thenReturn(categoryResponseDto);

            // then
            List<CategoryResponseDto> result = categoryService.getAllCategories();

            // return
            assertThat(result).hasSize(1);
            assertThat(result.getFirst()).isEqualTo(categoryResponseDto);
        }
    }

    @Nested
    class UnHappyCase {
        @Test
        void testCreateNewCategoryWithNameExisted_thenCreateCategory_returnCreateException() {
            // when
            when(categoryRepository.findByName(categoryCreateDto.getName())).thenReturn(Optional.of(category));

            // then, return
            assertThatThrownBy(() -> categoryService.createCategory(categoryCreateDto))
                    .isInstanceOf(AppException.class)
                    .hasMessageContaining(ErrorCode.CATEGORY_NAME_ALREADY_EXISTED.getMessage());
        }

        @Test
        void testCreateNewCategoryWithPrefixExisted_thenCreateCategory_returnCreateException() {
            // when
            when(categoryRepository.findByName(categoryCreateDto.getName())).thenReturn(Optional.empty());
            when(categoryRepository.findByCode(categoryCreateDto.getCode())).thenReturn(Optional.of(category));

            // then, return
            assertThatThrownBy(() -> categoryService.createCategory(categoryCreateDto))
                    .isInstanceOf(AppException.class)
                    .hasMessageContaining(ErrorCode.CATEGORY_PREFIX_ALREADY_EXISTED.getMessage());
        }
    }
}
