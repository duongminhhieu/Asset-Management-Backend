package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.ChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.repositories.LocationRepository;
import com.nashtech.rookie.asset_management_0701.utils.user.UserUtilImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.TypeInformation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.FirstChangePasswordRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserSearchDto;
import com.nashtech.rookie.asset_management_0701.dtos.responses.user.UserResponse;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.UserMapper;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.services.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Slf4j
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LocationRepository locationRepository;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;


    private User adminUsing;

    private Location adminLocation;

    private User userInDB;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    void setUp () {
        userInDB = new User();
        userInDB.setFirstName("first");
        userInDB.setLastName("Last");
        userInDB.setId(1L);
        userInDB.setStatus(EUserStatus.FIRST_LOGIN);
        userInDB.setGender(EGender.FEMALE);
        userInDB.setStaffCode("SD0001");

        adminLocation = new Location();
        adminLocation.setId(1L);
        adminLocation.setName("location");
        adminLocation.setCode("LCT");

        adminUsing = new User();
        adminUsing.setFirstName("admin");
        adminUsing.setLastName("admin");
        adminUsing.setUsername("abc.com");
        adminUsing.setId(2L);
        adminUsing.setLocation(adminLocation);

        changePasswordRequest = ChangePasswordRequest.builder()
                .password("Admin@123")
                .newPassword("Admin@1234")
                .build();
    }

    @Nested
    class HappyCase {
        @Test
        @WithMockUser(roles = "ADMIN", username = "adminName")
        void testCreateUserByAdminRole_validUser_shouldReturnUserResponse() {
            // Setup mocks
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName("Duy");
            userRequest.setLastName("Nguyen Hoang");
            userRequest.setDob(LocalDate.of(1990, 1, 1));
            userRequest.setJoinDate(LocalDate.of(2024, 6, 3));
            userRequest.setRole(ERole.ADMIN);
            userRequest.setLocationId(1L);
            when(userRepository.save(any())).thenReturn(userInDB);
            when(userRepository.findByUsername("adminName")).thenReturn(Optional.of(new User()));
            when(locationRepository.findById(any())).thenReturn(Optional.of(new Location()));
            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

            // Run the method
            UserResponse result = userService.createUser(userRequest);

            // Assertions
            assertEquals(userRequest.getFirstName(), result.getFirstName());
            assertEquals(userRequest.getLastName(), result.getLastName());
            assertEquals(userRequest.getDob(), result.getDob());
            assertEquals(userRequest.getJoinDate(), result.getJoinDate());
        }

        @Test
        @WithMockUser(roles = "USER", username = "adminName")
        void testCreateUserByUserRole_validUser_shouldReturnUserResponse() {
            // Setup mocks
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName("Duy");
            userRequest.setLastName("Nguyen Hoang");
            userRequest.setDob(LocalDate.of(1990, 1, 1));
            userRequest.setJoinDate(LocalDate.of(2024, 6, 3));
            userRequest.setRole(ERole.USER);

            when(userRepository.findByUsername("adminName")).thenReturn(Optional.of(adminUsing));
            when(userRepository.save(any())).thenReturn(userInDB);
            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

            // Run the method
            UserResponse result = userService.createUser(userRequest);

            // Assertions
            assertEquals(userRequest.getFirstName(), result.getFirstName());
            assertEquals(userRequest.getLastName(), result.getLastName());
            assertEquals(userRequest.getDob(), result.getDob());
            assertEquals(userRequest.getJoinDate(), result.getJoinDate());
        }

        @Test
        void testGenerateUsername_shouldReturnCorrectFormat() {
            // Given
            String firstName = "Duy";
            String lastName = "Nguyen";
            String expectedUsername = "duyn"; // Adjust based on your expected format

            // When
            String generatedUsername = userService.generateUsername(firstName, lastName);

            // Then
            assertEquals(expectedUsername, generatedUsername);
        }

        @ParameterizedTest
        @CsvSource({
            "null, 'ADMIN', 'firstName', 'DESC', 1, 20",
            "'first', 'ADMIN', 'firstName', 'DESC', 1, 20",
            "' ', 'STAFF', 'firstName', 'DESC', 1, 20"
        })
        @WithMockUser(username = "abc.com", roles = "ADMIN")
        void testGetAllUse_whenPassInValid_shouldReturnCorrectFormat (
                String searchString, String type, String sortBy, String sortDir, Integer pageNumber, Integer pageSize) {
            log.info("searchString:{}", searchString);
            log.info("type:{}", type);

            // set up
            var searchDto = new UserSearchDto(searchString, type, sortBy, sortDir, pageNumber, pageSize);
            var pageRequest = PageRequest.of(0, 20);
            var resultPage = new PageImpl<>(List.of(userInDB), pageRequest, 1);
            when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(resultPage);

            when(userRepository.findByUsername("abc.com")).thenReturn(Optional.of(adminUsing));

            // run
            var result = userService.getAllUser(searchDto);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("page", 1)
                    .hasFieldOrPropertyWithValue("total", 1L)
                    .hasFieldOrPropertyWithValue("itemsPerPage", 20);
            assertThat(result.getData().getFirst())
                    .usingRecursiveComparison()
                    .isEqualTo(userMapper.toUserResponse(userInDB));
        }

        @Test
        @WithMockUser(username = "test", roles = "ADMIN")
        void testChangePassword_whenValid_shouldReturnUserResponse () {
            // set up
            var changePasswordRequest = new FirstChangePasswordRequest("newPassword");
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(userInDB));
            when(passwordEncoder.encode("newPassword")).thenReturn("hashedPassword");
            when(userRepository.save(any(User.class))).thenReturn(userInDB);

            // run
            userService.firstChangePassword(changePasswordRequest);

            // verify
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @WithMockUser(username = "test", roles = "ADMIN")
        void testChangePassword_whenValid_shouldReturnSuccess () {
            // given
            when(passwordEncoder.matches(any(), any())).thenReturn(true);
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(userInDB));
            when(userRepository.save(any(User.class))).thenReturn(userInDB);

            // when
            userService.changePassword(changePasswordRequest);

            // then
            verify(userRepository, times(1)).save(any(User.class));
        }

    }

    @Nested
    class UnHappyCase {
        @Test
        @WithMockUser(roles = "ADMIN", username = "adminName")
        void testCreateUserJoinDateBeforeDob () {
            // Given
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName("Duy");
            userRequest.setLastName("Nguyen Hoang");
            userRequest.setDob(LocalDate.of(2000, 1, 1));
            userRequest.setJoinDate(LocalDate.of(1999, 12, 31));

            // When & Then
            AppException exception = assertThrows(AppException.class, () -> userService.createUser(userRequest));

            assertEquals(ErrorCode.JOIN_DATE_BEFORE_DOB, exception.getErrorCode());
        }

        @Test
        @WithMockUser(roles = "ADMIN", username = "adminName")
        void testCreateUserJoinDateWeekend () {
            // Given
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName("Duy");
            userRequest.setLastName("Nguyen");
            userRequest.setDob(LocalDate.of(2000, 1, 1));
            userRequest.setJoinDate(LocalDate.of(2023, 6, 17)); // Saturday

            // When & Then
            AppException exception = assertThrows(AppException.class, () -> userService.createUser(userRequest));

            assertEquals(ErrorCode.JOIN_DATE_WEEKEND, exception.getErrorCode());
        }

        @Test
        @WithMockUser(roles = "ADMIN", username = "adminUser")
        void testCreateUserAdminWithoutLocationId_shouldThrowAppException() {
            // Setup mocks
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName("Admin");
            userRequest.setLastName("User");
            userRequest.setDob(LocalDate.of(1980, 1, 1));
            userRequest.setJoinDate(LocalDate.of(2023, 6, 30));
            userRequest.setRole(ERole.ADMIN);
            userRequest.setLocationId(null); // Simulate missing locationId

            // Run and assert
            AppException exception = assertThrows(AppException.class, () -> userService.createUser(userRequest));
            assertEquals(ErrorCode.ADMIN_NULL_LOCATION, exception.getErrorCode());
        }

        @Test
        @WithMockUser(username = "abc.com", roles = "ADMIN")
        void testGetAllUse_whenPageNumberIs0_shouldThrowAppExcpetionWithErrorCodeBadPageable () {
            // set up
            var searchDto = new UserSearchDto("first", "ADMIN", "firstName", "DESC", 0, 20);
            var pageRequest = PageRequest.of(0, 20);
            var resultPage = new PageImpl<>(List.of(userInDB), pageRequest, 1);
            when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(resultPage);
            
            when(userRepository.findByUsername("abc.com")).thenReturn(Optional.of(adminUsing));

            // run
            var exception = assertThrows(AppException.class, () -> {
                userService.getAllUser(searchDto);
            });

            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_PAGEABLE);
        }

        @Test
        @WithMockUser(username = "abc.com", roles = "ADMIN")
        void testGetAllUse_whenSortbyDoesNotSupport_shouldReturnUsingDefault () {
            // set up
            var searchDto = new UserSearchDto("first", "ADMIN", "A", "DESC", 0, 20);
            var pageRequest = PageRequest.of(0, 20);
            var resultPage = new PageImpl<>(List.of(userInDB), pageRequest, 1);
            when(userRepository.findAll(any(Specification.class), 
                    argThat((Pageable page)->{
                        return !page.getSort().filter((order)->order.getProperty().equals("A")).isEmpty();
                    })))
                    .thenThrow(new PropertyReferenceException("A", TypeInformation.of(String.class), List.of()));
                    
            when(userRepository.findAll(any(Specification.class), 
                    argThat((Pageable page)->{
                        return page.getSort().filter((order)->order.getProperty().equals("A")).isEmpty();
                    })))
                    .thenReturn(resultPage);
            when(userRepository.findByUsername("abc.com")).thenReturn(Optional.of(adminUsing));

            // run
            var exception = assertThrows(AppException.class, () -> {
                userService.getAllUser(searchDto);
            });

            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_PAGEABLE);
        }


        @Test
        @WithMockUser(username = "test", roles = "ADMIN")
        void testFirstChangePassword_whenUserNotFound_shouldThrowAppException () {
            // set up
            var changePasswordRequest = new FirstChangePasswordRequest("newPassword");
            when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

            // run
            var exception = assertThrows(AppException.class, () -> {
                userService.firstChangePassword(changePasswordRequest);
            });

            // verify
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
        }

        @Test
        @WithMockUser(username = "test", roles = "ADMIN")
        void testFirstChangePassword_whenUserStatusNotFirstLogin_shouldThrowAppException () {
            // set up
            var changePasswordRequest = new FirstChangePasswordRequest("newPassword");
            userInDB.setStatus(EUserStatus.ACTIVE);
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(userInDB));

            // run
            var exception = assertThrows(AppException.class, () -> {
                userService.firstChangePassword(changePasswordRequest);
            });

            // verify
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_CHANGED);
        }

        @Test
        @WithMockUser(username = "test", roles = "ADMIN")
        void testChangePassword_whenPasswordSame_shouldThrowAppException () {
            // set up
            var changePasswordRequest = new ChangePasswordRequest("newPassword", "newPassword");
            when(passwordEncoder.matches(any(), any())).thenReturn(true);
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(userInDB));

            // run
            var exception = assertThrows(AppException.class, () -> {
                userService.changePassword(changePasswordRequest);
            });

            // verify
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.PASSWORD_SAME);
        }

        @Test
        @WithMockUser(username = "test", roles = "ADMIN")
        void testChangePassword_wrongPassword_shouldThrowAppException () {
            // set up
            var changePasswordRequest = new ChangePasswordRequest("password", "newPassword");
            when(passwordEncoder.matches(any(), any())).thenReturn(false);
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(userInDB));

            // run
            var exception = assertThrows(AppException.class, () -> {
                userService.changePassword(changePasswordRequest);
            });

            // verify
            assertThat(exception).hasFieldOrPropertyWithValue("errorCode", ErrorCode.WRONG_PASSWORD);
        }
    }
}
