package com.github.howaric.cache.enhancer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cache.enhanced")
public class EnhancedCacheProperties {

    private String enhancedCacheManagerName = EnhancedCacheManager.ENHANCED_CACHE_MANAGER;
    private long delayTime = 5000;

    public String getEnhancedCacheManagerName() {
        return enhancedCacheManagerName;
    }

    public void setEnhancedCacheManagerName(String enhancedCacheManagerName) {
        this.enhancedCacheManagerName = enhancedCacheManagerName;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }
}
