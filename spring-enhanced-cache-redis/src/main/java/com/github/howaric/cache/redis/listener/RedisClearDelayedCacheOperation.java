package com.github.howaric.cache.redis.listener;

import com.github.howaric.cache.core.DelayedCacheOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class RedisClearDelayedCacheOperation implements DelayedCacheOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClearDelayedCacheOperation.class);
    private final RedisCacheWriter redisCacheWriter;
    private final String name;
    private final byte[] pattern;

    public RedisClearDelayedCacheOperation(RedisCacheWriter redisCacheWriter, String name, byte[] pattern) {
        this.redisCacheWriter = redisCacheWriter;
        this.name = name;
        this.pattern = pattern;
    }

    @Override
    public void execute() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Delayed cleared cache: {}", new String(pattern));
        }
        redisCacheWriter.clean(name, pattern);
    }

}
