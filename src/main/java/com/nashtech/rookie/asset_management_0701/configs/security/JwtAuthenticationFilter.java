package com.nashtech.rookie.asset_management_0701.configs.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.exceptions.AppException;
import com.nashtech.rookie.asset_management_0701.exceptions.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        final String tokenPrefix = "Bearer ";

        // check if the authorization header is null or does not start with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authorizationHeader.replace(tokenPrefix, "");
        final String userName = jwtService.extractUserName(jwtToken);

        if (jwtService.extractExpiration(jwtToken).before(new Date())){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            EUserStatus status = jwtService.extractStatus(jwtToken);

            if (status == EUserStatus.FIRST_LOGIN
                    && !request.getRequestURI().endsWith("change-password")) {
                throw new AppException(ErrorCode.PASSWORD_NOT_CHANGE);
            }
            var securityData = jwtService.getSecurityData(userName);
            if (jwtService.userIsDisabled(securityData)) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }

            if (jwtService.tokenIsDisabled(securityData, jwtToken)){
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            ERole role = jwtService.extractRole(jwtToken);

            String scopes = jwtService.getAuthorities(role);


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userName, null, AuthorityUtils.createAuthorityList(scopes));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
