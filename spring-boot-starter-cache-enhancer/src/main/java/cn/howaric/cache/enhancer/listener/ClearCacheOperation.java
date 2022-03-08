package cn.howaric.cache.enhancer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

public class ClearCacheOperation implements DelayedCacheOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearCacheOperation.class);

    private final Cache cache;

    public ClearCacheOperation(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void execute() {
        cache.clear();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Re-clear cache");
        }
    }
}
