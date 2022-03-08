package cn.howaric.cache.enhancer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

public class ListenableCache implements Cache {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenableCache.class);

    private final Cache cache;
    private final CacheEventListener cacheEventListener;

    public ListenableCache(Cache cache, CacheEventListener cacheEventListener) {
        this.cache = cache;
        this.cacheEventListener = cacheEventListener;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return cache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return cache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return cache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.evict(key);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Evict cache");
        }
        try {
            cacheEventListener.evictCache(cache, key);
        } finally {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Evict cache delayed operation published");
            }
        }
    }

    @Override
    public void clear() {
        cache.clear();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Clear cache");
        }
        try {
            cacheEventListener.clearCache(cache);
        } finally {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Clear cache delayed operation published");
            }
        }
    }

}
