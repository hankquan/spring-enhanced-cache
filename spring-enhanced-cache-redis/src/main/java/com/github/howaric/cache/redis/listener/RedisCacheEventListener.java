package com.github.howaric.cache.redis.listener;

import com.github.howaric.cache.core.CacheEventListener;
import com.github.howaric.cache.core.DelayedCacheOperation;
import com.github.howaric.cache.core.delay.DelayTrigger;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class RedisCacheEventListener implements CacheEventListener {

    private final RedisCacheWriter redisCacheWriter;
    private final DelayTrigger<DelayedCacheOperation> delayTrigger;

    public RedisCacheEventListener(RedisCacheWriter redisCacheWriter, long delayTime) {
        this.redisCacheWriter = redisCacheWriter;
        this.delayTrigger = new DelayTrigger<>(DelayedCacheOperation::execute, delayTime);
    }

    @Override
    public void evictCache(String name, byte[] key) {
        delayTrigger.put(new RedisEvictDelayedCacheOperation(redisCacheWriter, name, key));
    }

    @Override
    public void clearCache(String name, byte[] pattern) {
        delayTrigger.put(new RedisClearDelayedCacheOperation(redisCacheWriter, name, pattern));
    }

    @Override
    public void stop() {
        delayTrigger.shutdown();
    }

}
