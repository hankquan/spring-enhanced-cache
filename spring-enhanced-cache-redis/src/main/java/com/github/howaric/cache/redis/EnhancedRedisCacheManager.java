package com.github.howaric.cache.redis;

import com.github.howaric.cache.core.CacheEventListener;
import com.github.howaric.cache.redis.listener.RedisCacheEventListener;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.Assert;

public class EnhancedRedisCacheManager extends RedisCacheManager implements DisposableBean {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration redisCacheConfiguration;
    private final CacheEventListener cacheEventListener;

    public EnhancedRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, long delayTime) {
        super(cacheWriter, defaultCacheConfiguration);
        Assert.isTrue(delayTime > 0, "delayTime must be positive.");
        this.cacheWriter = cacheWriter;
        this.redisCacheConfiguration = defaultCacheConfiguration;
        this.cacheEventListener = new RedisCacheEventListener(cacheWriter, delayTime);
    }

    @Override
    public void destroy() throws Exception {
        cacheEventListener.stop();
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new ListenableRedisCache(name, cacheWriter,
                cacheConfig != null ? cacheConfig : redisCacheConfiguration, cacheEventListener);
    }

}
