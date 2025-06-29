package com.hsbc.transaction.config

import spock.lang.Specification

class AsyncConfigSpec extends Specification {
    def config = new AsyncConfig()

    def "should create asyncTaskExecutor bean"() {
        when:
        def executor = config.asyncExecutor()
        then:
        executor != null
        executor.corePoolSize == 50
        executor.maxPoolSize == 50
        executor.queueCapacity == 1000
        executor.threadNamePrefix == "AsyncTx-"
    }
} 