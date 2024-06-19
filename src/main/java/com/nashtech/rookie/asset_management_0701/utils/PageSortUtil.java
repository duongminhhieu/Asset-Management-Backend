package com.nashtech.rookie.asset_management_0701.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;

public final class PageSortUtil {
    private PageSortUtil () {
    }

    public static Pageable createPageRequest (Integer pageNumber, Integer pageSize, String sortBy,
                                              Sort.Direction sortDirection) {
        if (pageNumber == null || pageSize == null || sortBy == null) {
            throw new AppException(ErrorCode.INVALID_PAGEABLE);
        }
        try {
            Sort sort = Sort.by(sortDirection, sortBy);
            return PageRequest.of(pageNumber, pageSize, sort);
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
            throw new AppException(ErrorCode.INVALID_PAGEABLE);
        }
    }
}
