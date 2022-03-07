package com.github.howaric.cache.enhancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;

import java.util.Map;

public class EnhancedCacheConfig extends CachingConfigurerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnhancedCacheConfig.class);

    private final DefaultListableBeanFactory beanFactory;

    public EnhancedCacheConfig(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public CacheManager cacheManager() {
        Map<String, CacheManager> cacheManagerMap = beanFactory.getBeansOfType(CacheManager.class);
        if (cacheManagerMap.isEmpty()) {
            return super.cacheManager();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loaded cache manager: {}", cacheManagerMap);
        }
        for (Map.Entry<String, CacheManager> entry : cacheManagerMap.entrySet()) {
            if (!(entry.getValue() instanceof EnhancedCacheManager)) {
                return entry.getValue();
            }
        }
        return super.cacheManager();
    }

}
