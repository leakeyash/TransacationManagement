package com.hsbc.transaction.config

import org.springframework.cache.CacheManager
import spock.lang.Specification

class CacheConfigSpec extends Specification {
    def config = new CacheConfig()

    def "should create customCacheManager bean"() {
        when:
        CacheManager manager = config.customCacheManager()
        then:
        manager != null
    }

    def "should create accountCache bean"() {
        expect:
        config.accountCache().name == "accountCache"
    }

    def "should create transactionCache bean"() {
        expect:
        config.transactionCache().name == "transactionCache"
    }
} 