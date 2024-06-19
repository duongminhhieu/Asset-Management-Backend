package com.nashtech.rookie.asset_management_0701.dtos.responses.user;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String staffCode;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate joinDate;
    private LocalDate dob;
    private EGender gender;
    private EUserStatus status;
    private ERole type;
    private LocationResponse location;
}
