package com.github.howaric.cache.redis.listener;

import com.github.howaric.cache.core.DelayedCacheOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class RedisEvictDelayedCacheOperation implements DelayedCacheOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEvictDelayedCacheOperation.class);
    private final RedisCacheWriter redisCacheWriter;
    private final String name;
    private final byte[] key;

    public RedisEvictDelayedCacheOperation(RedisCacheWriter redisCacheWriter, String name, byte[] key) {
        this.redisCacheWriter = redisCacheWriter;
        this.name = name;
        this.key = key;
    }

    @Override
    public void execute() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Delayed cleared cache: {}", new String(key));
        }
        redisCacheWriter.remove(name, key);
    }

}
