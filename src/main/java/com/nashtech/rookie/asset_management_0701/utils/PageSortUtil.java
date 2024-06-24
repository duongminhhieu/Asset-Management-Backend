package com.nashtech.rookie.asset_management_0701.utils;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.nashtech.rookie.asset_management_0701.constants.DefaultSortOptions;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;

public final class PageSortUtil {
    private PageSortUtil () {
    }
    public static Pageable createPageRequest (Integer pageNumber, Integer pageSize, Sort sort) {
        return PageRequest.of(pageNumber - 1, pageSize > 100 ? 100 : pageSize, sort);
    }

    public static Pageable createPageRequest (Integer pageNumber, Integer pageSize, String sortBy,
                                              Sort.Direction sortDirection, String defaulSortBy) {
        if (pageNumber == null || pageSize == null || sortBy == null) {
            throw new AppException(ErrorCode.INVALID_PAGEABLE);
        }
        try {
            if (sortBy.isBlank()){
                sortBy = defaulSortBy;
            }
            Sort sort = Sort.by(sortDirection, sortBy);
            if (sortBy.equals("fullName")){
                sort = Sort.by(List.of(new Order(sortDirection, "firstName"), new Order(sortDirection, "lastName")));
            }
            return PageRequest.of(pageNumber, Math.min(pageSize, DefaultSortOptions.MAX_PAGE_SIZE), sort);
        }
        catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_PAGEABLE);
        }
    }

    public static Sort.Direction parseSortDirection (String sortDirection) {
        try {
            return Sort.Direction.fromString(sortDirection);
        }
        catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_SORT_DIR);
        }
    }
    public static int parsePageValue (String value, int defaultValue) {
        try {
            int parsedValue = Integer.parseInt(value);
            return Math.max(parsedValue, 1);
        }
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
