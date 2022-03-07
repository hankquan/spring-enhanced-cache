package com.github.howaric.cache.enhancer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

public class EvictCacheOperation implements DelayedCacheOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvictCacheOperation.class);

    private final Cache cache;
    private final Object key;

    public EvictCacheOperation(Cache cache, Object key) {
        this.cache = cache;
        this.key = key;
    }

    @Override
    public void execute() {
        cache.evict(key);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Re-evict cache");
        }
    }

}
