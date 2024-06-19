package com.nashtech.rookie.asset_management_0701.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.nashtech.rookie.asset_management_0701.utils.asset_utils.AssetUtil;

class AssetUtilsTest {

    @Test
    void testGenerateAssetCode() {
        Long count = 1L;
        String name = "LP";

        // Expected result
        String expected = "LP000001";

        // Call the method to test
        String result = AssetUtil.generateAssetCode(count, name);

        // Verify the result
        assertEquals(expected, result);
    }
}
