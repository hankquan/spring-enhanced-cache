# spring-enhanced-cache project

[![CI](https://github.com/howaric/spring-enhanced-cache/actions/workflows/maven-publish.yml/badge.svg?branch=main)](https://github.com/howaric/spring-enhanced-cache/actions/workflows/maven-publish.yml)
[![License](https://img.shields.io/github/license/howaric/spring-enhanced-cache.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![NetflixOSS Lifecycle](https://img.shields.io/osslifecycle/howaric/spring-enhanced-cache.svg)]()
[![Github release](https://img.shields.io/github/v/release/howaric/spring-enhanced-cache.svg)](https://GitHub.com/howaric/spring-enhanced-cache/releases)

## Overview

This project aims to enhance springboot cache without code invasion in high concurrency scenarios.

## Roadmap

- [x] Delayed re-eviction of cache by @CacheEvict
- [ ] Retry eviction of cache when failed to delete cache
- [ ] Random cache TTL in a specific range

## User Guide

Add dependency as below, spring-boot-starter-cache won't be needed when this dependency is added in your project.

```xml
<dependency>
    <groupId>cn.howaric.cache</groupId>
    <artifactId>spring-boot-starter-cache-enhancer</artifactId>
    <version>0.0.1</version>
</dependency>
```

Add a cache implementation such as redis:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

You can define your own CacheManager, if not, a default CacheManager will be created. By the way, this default CacheManager is not created by the enhancer but spring cache. 

The enhancer will create an EnhancedCacheManager and inject to spring container, the EnhancedCacheManager's name is defined in `cn.howaric.cache.enhancer.EnhancedCacheManager.ENHANCED_CACHE_MANAGER`, its name can also be specified by `spring.cache.enhanced.enhancedCacheManagerName` in spring application.yml.

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 10000 # time unit is millis
    enhanced:
      enhanced-cache-manager-name: "enhancedCacheManager" # name of the cache manager
      delay-time: 2000 # means deleting cache eviction again after 2s, default is 5s
```

Then you can just use this CacheManager in @CacheEvict annotation, and @CacheEvict will trigger a delayed eviction of the cache in the specific delay time. 

```java
@CacheEvict(key = "#p0.username", cacheManager = EnhancedCacheManager.ENHANCED_CACHE_MANAGER)
public void updateUser(User user) {
  //update user
}
```

## Trouleshooting

1. How to make sure the EnhancedCacheManager really trigger the delayed eviction of cache?

You can just open the debug log for package `cn.howaric.cache`, then you will see the related logs.

```yaml
logging:
  level:
    cn.howaric.cache: debug
```

Logs example,

```tex
2022-03-07 21:56:40.780 DEBUG 85337 --- [nio-8080-exec-1] c.g.h.c.e.listener.ListenableCache       : Evict cache
2022-03-07 21:56:40.782 DEBUG 85337 --- [nio-8080-exec-1] c.g.h.c.e.listener.ListenableCache       : Evict cache delayed operation published
2022-03-07 21:56:42.804 DEBUG 85337 --- [pool-1-thread-1] c.g.h.c.e.listener.EvictCacheOperation   : Re-evict cache
```
