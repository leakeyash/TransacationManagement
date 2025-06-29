package com.hsbc.transaction.exception

import spock.lang.Specification

class DuplicateTransactionExceptionSpec extends Specification {
    def "should create exception with message"() {
        when:
        def ex = new DuplicateTransactionException("msg")
        then:
        ex.message == "msg"
    }
} 