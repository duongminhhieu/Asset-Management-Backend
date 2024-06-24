package com.nashtech.rookie.asset_management_0701.services;

import com.nashtech.rookie.asset_management_0701.dtos.requests.location.LocationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.location.LocationResponse;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.LocationMapper;
import com.nashtech.rookie.asset_management_0701.repositories.LocationRepository;
import com.nashtech.rookie.asset_management_0701.services.location.LocationServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LocationServiceTest {
    @MockBean
    private LocationRepository locationRepository;


    @Autowired
    private LocationServiceImpl locationService;

    @Nested
    class HappyCase {
        @Test
        void testGetAllLocation_locationsExist_shouldReturnLocationResponses() {
            // Given
            List<Location> locations = new ArrayList<>();
            Location location1 = new Location();
            location1.setId(1L);
            location1.setName("Tay Ninh");
            location1.setCode(("TN"));

            Location location2 = new Location();
            location2.setId(2L);
            location2.setName("Vung Tau");
            location2.setCode(("VT"));

            locations.add(location1);
            locations.add(location2);

            when(locationRepository.findAll()).thenReturn(locations);

            // When
            List<LocationResponse> result = locationService.getAllLocation();

            // Then
            assertEquals(2, result.size());
            assertEquals("Tay Ninh", result.get(0).getName());
            assertEquals("TN", result.get(0).getCode());
            assertEquals("Vung Tau", result.get(1).getName());
            assertEquals("VT", result.get(1).getCode());
        }

        @Test
        void testCreateLocation_validLocationRequest_shouldReturnLocationResponse() {
            // Given
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setName("Tay Ninh");
            locationRequest.setCode("TN");

            Location location = new Location();
            location.setId(1L);
            location.setName("Tay Ninh");
            location.setCode(("TN"));

            when(locationRepository.save(any())).thenReturn(location);

            // When
            LocationResponse result = locationService.createLocation(locationRequest);

            // Then
            assertEquals("Tay Ninh", result.getName());
            assertEquals("TN", result.getCode());
        }
    }

    @Nested
    class UnHappyCase {
        @Test
        void testCreateLocation_duplicateLocationName_shouldThrowAppException() {
            // Given
            LocationRequest locationRequest = new LocationRequest("Tay Ninh", "TN");
            when(locationRepository.existsByName(locationRequest.getName())).thenReturn(true);

            // When & Then
            AppException exception = assertThrows(AppException.class, () -> locationService.createLocation(locationRequest));
            assertEquals(ErrorCode.LOCATION_NAME_ALREADY_EXISTED, exception.getErrorCode());
        }

        @Test
        void testCreateLocation_duplicateLocationCode_shouldThrowAppException() {
            LocationRequest locationRequest = new LocationRequest("Tay Ninh", "TN");
            when(locationRepository.existsByName(locationRequest.getName())).thenReturn(false);
            when(locationRepository.existsByCode(locationRequest.getCode())).thenReturn(true);

            // When & Then
            AppException exception = assertThrows(AppException.class, () -> locationService.createLocation(locationRequest));
            assertEquals(ErrorCode.LOCATION_CODE_ALREADY_EXISTED, exception.getErrorCode());
        }

    }
}
