# spring-enhanced-cache project

## Overview

This project aims to enhance springboot cache without code invasion.  
The enhancement could make the cache implementation adapt to high concurrency scenarios.

## Roadmap

- [x] Delayed double eviction of cache by @CacheEvict
- [ ] Retry eviction of cache when failed to delete cache
- [ ] Random cache TTL in a specific range

## User Guide

### 1.Add dependency

The release is not published yet, you can clone the repo and compile locally as workaround.

### 2.Declare bean EnhancedRedisCacheManager

```java
@Configuration
public class EnhancedRedisCacheConfiguration {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        //3000 means the cache will be deleted again after 3 seconds.
        return new EnhancedRedisCacheManager(cacheWriter, redisCacheConfiguration, 3000);
    }
}
```
