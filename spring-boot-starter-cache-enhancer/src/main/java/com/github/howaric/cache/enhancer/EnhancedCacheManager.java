package com.github.howaric.cache.enhancer;

import com.github.howaric.cache.enhancer.delay.DelayTrigger;
import com.github.howaric.cache.enhancer.listener.CacheEventListener;
import com.github.howaric.cache.enhancer.listener.DelayedCacheOperation;
import com.github.howaric.cache.enhancer.listener.ListenableCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.Collection;

public class EnhancedCacheManager implements CacheManager {

    public static final String ENHANCED_CACHE_MANAGER = "enhancedCacheManager";
    private final CacheManager cacheManager;
    private final CacheEventListener cacheEventListener;

    public EnhancedCacheManager(CacheManager cacheManager, EnhancedCacheProperties enhancedCacheProperties) {
        Assert.isTrue(enhancedCacheProperties.getDelayTime() > 0, "delay time must be positive number.");
        DelayTrigger<DelayedCacheOperation> delayTrigger = new DelayTrigger<>(DelayedCacheOperation::execute, enhancedCacheProperties.getDelayTime());
        this.cacheEventListener = new CacheEventListener(delayTrigger);
        this.cacheManager = cacheManager;
    }

    @Override
    public Cache getCache(String name) {
        return new ListenableCache(cacheManager.getCache(name), cacheEventListener);
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }
}
