package com.nashtech.rookie.asset_management_0701.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    @Builder.Default
    private int internalCode = 1000;
    private String message;
    private T result;
}
