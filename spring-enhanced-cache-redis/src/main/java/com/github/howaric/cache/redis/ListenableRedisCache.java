package com.github.howaric.cache.redis;

import com.github.howaric.cache.core.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class ListenableRedisCache extends RedisCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenableRedisCache.class);
    private final CacheEventListener cacheEventListener;
    private final RedisCacheConfiguration cacheConfig;
    private final RedisCacheWriter cacheWriter;

    protected ListenableRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, CacheEventListener cacheEventListener) {
        super(name, cacheWriter, cacheConfig);
        this.cacheEventListener = cacheEventListener;
        this.cacheConfig = cacheConfig;
        this.cacheWriter = cacheWriter;
    }

    @Override
    public void evict(Object key) {
        String cacheKey = createCacheKey(key);
        try {
            super.evict(key);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Evicted cache: {}", cacheKey);
            }
        } finally {
            cacheEventListener.evictCache(getName(), serializeCacheKey(cacheKey));
        }
    }

    @Override
    public void clear() {
        String cacheKey = createCacheKey("*");
        try {
            super.clear();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cleared cache: {}", cacheKey);
            }
        } finally {
            cacheEventListener.clearCache(getName(), cacheConfig.getConversionService().convert(cacheKey, byte[].class));
        }
    }
}
