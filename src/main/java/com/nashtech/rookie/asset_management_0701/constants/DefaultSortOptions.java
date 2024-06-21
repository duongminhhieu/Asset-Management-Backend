package com.nashtech.rookie.asset_management_0701.constants;

import java.util.List;

public final class DefaultSortOptions {
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUMBER = 1;
    public static final Integer MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_USER_SORT_BY = "firstName";
    public static final String DEFAULT_SORT_ORDER = "ASC";
    public static final List<String> SYSTEM_ROLE = List.of("ADMIN", "STAFF");
    private DefaultSortOptions () {
    }
}
