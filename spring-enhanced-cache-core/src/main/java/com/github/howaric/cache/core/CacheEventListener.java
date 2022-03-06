package com.github.howaric.cache.core;

public interface CacheEventListener {

    void evictCache(String name, byte[] key);

    void clearCache(String name, byte[] pattern);

    void stop();

}