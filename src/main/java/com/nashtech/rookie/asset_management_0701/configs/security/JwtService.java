package com.nashtech.rookie.asset_management_0701.configs.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nashtech.rookie.asset_management_0701.dtos.security.UserSecurityData;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import com.nashtech.rookie.asset_management_0701.repositories.InvalidTokenRepository;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.services.user.UserSpecification;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {


    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.expiration}")
    private Long expiration;

    private final InvalidTokenRepository invalidTokenRepository;

    private final UserRepository userRepository;

    private Claims extractAllClaims ( String jwtToken) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public <T> T extractClaim ( String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String extractUserName ( String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public String extractIdToken ( String token) {
        return extractClaim(token, Claims::getId);
    }

    public boolean isTokenValid ( String jwtToken) {
        String idToken = extractIdToken(jwtToken);
        return !invalidTokenRepository.existsByIdToken(idToken) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired ( String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    public Date extractExpiration (String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public EUserStatus extractStatus (String jwtToken) {
        return EUserStatus.valueOf((String) extractClaim(jwtToken, claims -> claims.get("status")));
    }

    public ERole extractRole (String jwtToken) {
        return ERole.valueOf((String) extractClaim(jwtToken, claims -> claims.get("type")));
    }

    public String generateToken (User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("status", user.getStatus());
        claims.put("type", user.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(
                        Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setId(UUID.randomUUID().toString())
                .compact();
    }

    private Key getSignInKey () {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getAuthorities (ERole role) {
        return "ROLE_" + role.toString(); // "ROLE_USER READ WRITE"
    }

    @Cacheable(value = "userDisable")
    public UserSecurityData getSecurityData (String username){
        User user = userRepository.findOne(
            Specification.where(UserSpecification.tokenNotExpireForUsername(username)))
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return UserSecurityData.builder()
            .id(user.getId())
            .status(user.getStatus())
            .invalidTokens(user.getInvalidTokens()
                    .stream().map(token -> token.getIdToken()).collect(Collectors.toSet()))
            .build();
    }

    public boolean userIsDisabled (UserSecurityData data) {
        return data.getStatus().equals(EUserStatus.DISABLED);
    }

    public boolean tokenIsDisabled (UserSecurityData data, String token) {
        return data.getInvalidTokens().contains(token);
    }
}
