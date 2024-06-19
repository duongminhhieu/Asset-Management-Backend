package com.nashtech.rookie.asset_management_0701.utils.asset_utils;

public final class AssetUtil {

    private AssetUtil () {
    }

    public static String generateAssetCode (Long count, String name) {
        return String.format("%s%06d", name, count);
    }
}
