package com.hsbc.transaction.exception

import com.hsbc.transaction.exception.TransactionNotSupported
import spock.lang.Specification

class TransactionNotSupportedSpec extends Specification {
    def "should create exception with message"() {
        when:
        def ex = new TransactionNotSupported("msg")
        then:
        ex.message == "msg"
    }
} 