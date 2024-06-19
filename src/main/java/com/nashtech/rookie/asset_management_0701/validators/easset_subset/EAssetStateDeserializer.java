package com.nashtech.rookie.asset_management_0701.validators.easset_subset;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nashtech.rookie.asset_management_0701.enums.EAssetState;

public class EAssetStateDeserializer extends JsonDeserializer<EAssetState> {

    @Override
    public EAssetState deserialize (JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return Arrays.stream(EAssetState.values())
                .filter(enumValue -> enumValue.name().equals(value))
                .findFirst()
                .orElse(EAssetState.RECYCLED);
    }
}
