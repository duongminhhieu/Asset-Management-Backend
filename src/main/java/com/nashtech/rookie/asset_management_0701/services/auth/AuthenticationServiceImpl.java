package com.nashtech.rookie.asset_management_0701.services.auth;


import java.time.Instant;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookie.asset_management_0701.configs.security.JwtService;
import com.nashtech.rookie.asset_management_0701.dtos.requests.auth.AuthenticationRequest;
import com.nashtech.rookie.asset_management_0701.dtos.responses.auth.AuthenticationResponse;
import com.nashtech.rookie.asset_management_0701.entities.InvalidToken;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.mappers.UserMapper;
import com.nashtech.rookie.asset_management_0701.repositories.InvalidTokenRepository;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.utils.auth_util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AuthUtil authUtil;

    private final InvalidTokenRepository invalidTokenRepository;

    private final CacheManager cacheManager;

    @Override
    public AuthenticationResponse login (AuthenticationRequest request) {

        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_OR_PASSWORD_INCORRECT));

        if (!passwordEncoder.matches(request.getPassword(), user.getHashPassword())) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_INCORRECT);
        }

        // check if user is active
        if (user.getStatus() == EUserStatus.DISABLED) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        var token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .user(userMapper.toUserResponse(user))
                .token(token)
                .build();
    }

    @Transactional
    public void logout (String token) {
        InvalidToken invalidToken =
            new InvalidToken(token, jwtService.extractExpiration(token).toInstant(), authUtil.getCurrentUser());

        invalidTokenRepository.save(invalidToken);

        cacheManager.getCache("userDisable").evictIfPresent(authUtil.getCurrentUserName());
    }

    @CacheEvict(value = "userDisable", allEntries = true)
    @Scheduled(fixedDelayString = "${application.jwt.expiration}")
    @Transactional
    public void cleanInvalidToken () {
        invalidTokenRepository.deleteExpiredTokens(Instant.now());
    }
}
