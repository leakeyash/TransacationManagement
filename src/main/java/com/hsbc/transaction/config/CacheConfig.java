package com.hsbc.transaction.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
  @Primary
  @Bean("customCacheManager")
  public CacheManager customCacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    List<Cache> cacheList = new ArrayList<>();
    cacheList.add(accountCache());
    cacheList.add(transactionCache());
    simpleCacheManager.setCaches(cacheList);
    return simpleCacheManager;
  }

  public Cache accountCache() {
    return new CaffeineCache(
        "accountCache",
        Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).maximumSize(100_000).build(),
        true);
  }

  public Cache transactionCache() {
    return new CaffeineCache(
        "transactionCache",
        Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).maximumSize(100_000).build(),
        true);
  }
}
