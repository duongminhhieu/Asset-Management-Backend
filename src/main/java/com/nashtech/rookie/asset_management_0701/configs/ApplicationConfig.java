package com.nashtech.rookie.asset_management_0701.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nashtech.rookie.asset_management_0701.constants.PredefinedLocation;
import com.nashtech.rookie.asset_management_0701.entities.Location;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import com.nashtech.rookie.asset_management_0701.repositories.LocationRepository;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Value("${application.admin.default.username}")
    private String adminUsername;

    @Value("${application.admin.default.password}")
    private String adminPassword;

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public OpenAPI customizeOpenAPI () {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner () {
        log.info("Initializing application.....");
        return args -> {
            Location adminLocation = null;

            if (locationRepository.count() == 0) {
                locationRepository.save(Location.builder()
                        .name(PredefinedLocation.HN_NAME)
                        .code(PredefinedLocation.HN_CODE)
                        .build());

                locationRepository.save(Location.builder()
                        .name(PredefinedLocation.DN_NAME)
                        .code(PredefinedLocation.DN_CODE)
                        .build());

                adminLocation = locationRepository.save(Location.builder()
                        .name(PredefinedLocation.HCM_NAME)
                        .code(PredefinedLocation.HCM_CODE)
                        .build());
            }

            if (!userRepository.existsByUsername(adminUsername)) {

                User user = User.builder()
                        .username(adminUsername)
                        .hashPassword(passwordEncoder().encode(adminPassword))
                        .role(ERole.ADMIN)
                        .location(adminLocation)
                        .status(EUserStatus.ACTIVE)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
