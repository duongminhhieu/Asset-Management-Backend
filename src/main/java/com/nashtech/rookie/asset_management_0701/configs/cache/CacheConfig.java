package com.nashtech.rookie.asset_management_0701.configs.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager (){
        return new ConcurrentMapCacheManager();
    }

    @CacheEvict(value = "userDisable", allEntries = true)
    @Scheduled(fixedRateString = "${application.jwt.expiration}")
    public void emptyHotelsCache () {
    }
}
