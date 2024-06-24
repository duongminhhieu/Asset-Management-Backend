package com.nashtech.rookie.asset_management_0701.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PageSortUtilTest {

    @Test
    void parseSortDirection_validSortDirection_returnSortDirection() {
        // Given
        String sortDirection = "ASC";
        // When
        var result = PageSortUtil.parseSortDirection(sortDirection);
        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void parseSortDirection_invalidSortDirection_returnSortDirection() {
        // Given
        String sortDirection = "INVALID";
        // When
        // Then
        assertThatThrownBy(() -> PageSortUtil.parseSortDirection(sortDirection));
    }

    @Test
    void createPageRequest_validPageNumberPageSizeSort_returnPageable() {
        // Given
        Integer pageNumber = 1;
        Integer pageSize = 10;
        var sort = Sort.by(Sort.Direction.ASC, "name");
        // When
        var result = PageSortUtil.createPageRequest(pageNumber, pageSize, sort);
        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void createPageRequest_invalidPageNumber_returnPageable() {
        // Given
        Integer pageNumber = null;
        Integer pageSize = 10;
        var sort = Sort.by(Sort.Direction.ASC, "name");
        // When
        // Then
        assertThatThrownBy(() -> PageSortUtil.createPageRequest(pageNumber, pageSize, sort));
    }

    @Test
    void parsePageValue_validValue_returnValue() {
        // Given
        String value = "1";
        int defaultValue = 10;
        // When
        var result = PageSortUtil.parsePageValue(value, defaultValue);
        // Then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void parsePageValue_invalidValue_returnValue() {
        // Given
        String value = "INVALID";
        int defaultValue = 10;
        // When
        var result = PageSortUtil.parsePageValue(value, defaultValue);
        // Then
        assertThat(result).isEqualTo(10);
    }

}
