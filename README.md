# spring-enhanced-cache project

[![CI](https://github.com/howaric/spring-enhanced-cache/actions/workflows/maven-publish.yml/badge.svg?branch=main)](https://github.com/howaric/spring-enhanced-cache/actions/workflows/maven-publish.yml)
[![License](https://img.shields.io/github/license/howaric/spring-enhanced-cache.svg)](http://www.apache.org/licenses/LICENSE-2.0)
![Maven Central](https://img.shields.io/maven-central/v/cn.howaric.cache/spring-enhanced-cache)

## Overview

This project aims to enhance springboot cache without code invasion in high concurrency scenarios.

## Features

- [X]  Support to evict cache again in a delay time by @CacheEvict
- [ ]  Provide retry mechanism when eviction of cache failed
- [ ]  Random cache TTL in a specific range

## Usage

### Add dependency

Latest version: [![Github release](https://img.shields.io/github/v/release/howaric/spring-enhanced-cache.svg)](https://GitHub.com/howaric/spring-enhanced-cache/releases)

```xml
<dependency>
    <groupId>cn.howaric.cache</groupId>
    <artifactId>spring-boot-starter-cache-enhancer</artifactId>
    <version>${latest.version}</version>
</dependency>
```

> spring-boot-starter-cache won't be needed when this dependency is added in your project.

Add a cache implementation, here uses redis as an example.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

You can define your own CacheManager, or use the default CacheManager which created by spring cache AutoConfiguration.

### Setup configuration

With spring-enhanced-cache, there will be an EnhancedCacheManager created and injected as a spring bean automaticly. The default name of the EnhancedCacheManager is `cn.howaric.cache.enhancer.EnhancedCacheManager.ENHANCED_CACHE_MANAGER`, it can also be specified by `spring.cache.enhanced.enhancedCacheManagerName` in spring application.yml.

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 10000 # time unit is millis
    enhancer:
      # enhanced-cache-manager-name: "customEnhancedCacheManager" # name of the cache manager
      delay-time: 2000 # means deleting cache eviction again after 2s, default is 5s
```

By specifying this enhanced CacheManager in @CacheEvict annotation, it will trigger a delayed eviction of the cache in the specific delay time.

```java
@CacheEvict(key = "#p0.username", cacheManager = EnhancedCacheManager.ENHANCED_CACHE_MANAGER)
public void updateUser(User user) {
  //update user
}
```

## Troubleshooting

1. How to make sure the EnhancedCacheManager really trigger the delayed eviction of cache?

You can just open the debug log for package `cn.howaric.cache.enhancer`, then you will see the related logs.

```yaml
logging:
  level:
    cn.howaric.cache.enhancer: debug
```

Logs example,

```tex
2022-03-07 21:56:40.780 DEBUG 85337 --- [nio-8080-exec-1] c.h.c.e.listener.ListenableCache       : Evict cache
2022-03-07 21:56:40.782 DEBUG 85337 --- [nio-8080-exec-1] c.h.c.e.listener.ListenableCache       : Evict cache delayed operation published
2022-03-07 21:56:42.804 DEBUG 85337 --- [pool-1-thread-1] c.h.c.e.listener.EvictCacheOperation   : Re-evict cache
```
