package com.github.howaric.cache.enhancer.listener;

import com.github.howaric.cache.enhancer.delay.DelayTrigger;
import org.springframework.cache.Cache;

public class CacheEventListener {

    private final DelayTrigger<DelayedCacheOperation> delayTrigger;

    public CacheEventListener(DelayTrigger<DelayedCacheOperation> delayTrigger) {
        this.delayTrigger = delayTrigger;
    }

    public void evictCache(Cache cache, Object key) {
        delayTrigger.put(new EvictCacheOperation(cache, key));
    }

    public void clearCache(Cache cache) {
        delayTrigger.put(new ClearCacheOperation(cache));
    }

    public void stop() {
        delayTrigger.shutdown();
    }

}
