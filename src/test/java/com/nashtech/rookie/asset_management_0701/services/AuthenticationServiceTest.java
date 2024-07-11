package com.nashtech.rookie.asset_management_0701.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nashtech.rookie.asset_management_0701.configs.security.JwtService;
import com.nashtech.rookie.asset_management_0701.dtos.requests.auth.AuthenticationRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.repositories.InvalidTokenRepository;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.services.auth.AuthenticationServiceImpl;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InvalidTokenRepository invalidTokenRepository;

    @MockBean
    private AuthUtil authUtil;

    private User user;

    private AuthenticationRequest authRequest;

    @BeforeEach
    void SetUp () {
        user = User.builder()
                .username("admin")
                .hashPassword("123456")
                .firstName("Khoa")
                .lastName("Do")
                .build();
        authRequest = new AuthenticationRequest("admin", "123456");
    }

    @Nested
    class HappyCase {
        @Test
        void login_validRequest_success () {
            // Given
            when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("123456", user.getHashPassword())).thenReturn(true);
            when(jwtService.generateToken(user)).thenReturn("token");

            // When

            var authenticationResponse = authenticationService.login(authRequest);

            // Then

            assertThat(authenticationResponse).isNotNull().hasFieldOrPropertyWithValue("token", "token");
        }

        @Test
        void testLogout_whenSuccess () {
            String token = "token";
            Date expiryDate = new Date();
            when(jwtService.extractExpiration(token)).thenReturn(expiryDate);
            when(authUtil.getCurrentUser()).thenReturn(user);
            when(authUtil.getCurrentUserName()).thenReturn("admin");
            when(invalidTokenRepository.save(any())).thenReturn(null);

            authenticationService.logout(token);

            verify(invalidTokenRepository, times(1)).save(any());

        }
    }

    @Nested
    class UnhappyCase {
        @Test
        void login_invalidUserName_throwException () {
            // Given

            when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
            when(passwordEncoder.matches("123456", user.getHashPassword())).thenReturn(true);

            // When

            var exception = assertThrows(AppException.class, () -> authenticationService.login(authRequest));

            // Then
            assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1006);
            assertThat(exception.getErrorCode().getMessage()).isEqualTo("Username or password is incorrect");
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(jwtService, never()).generateToken(any());
        }

        @Test
        void login_invalidPassword_thorwException () {
            // Given
            when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("123456", user.getHashPassword())).thenReturn(false);

            // When
            var exception = assertThrows(AppException.class, () -> authenticationService.login(authRequest));

            // Then
            assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1006);
            assertThat(exception.getErrorCode().getMessage()).isEqualTo("Username or password is incorrect");
            verify(jwtService, never()).generateToken(any());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        }

        @Test
        void login_userIsNotActive_throwException () {
            // Given
            user.setStatus(EUserStatus.DISABLED);
            when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("123456", user.getHashPassword())).thenReturn(true);

            // When
            var exception = assertThrows(AppException.class, () -> authenticationService.login(authRequest));

            // Then
            assertThat(exception.getErrorCode().getInternalCode()).isEqualTo(1007);
            assertThat(exception.getErrorCode().getMessage()).isEqualTo("User is not active");
            verify(jwtService, never()).generateToken(any());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        }

        @Test
        void testCleanUpDb () {
            // Call the method under test
            authenticationService.cleanInvalidToken();

            // Verify that deleteExpiredTokens() was called on invalidTokenRepository with the current time
            verify(invalidTokenRepository, atLeastOnce()).deleteExpiredTokens(any(Instant.class));
        }
    }
}
