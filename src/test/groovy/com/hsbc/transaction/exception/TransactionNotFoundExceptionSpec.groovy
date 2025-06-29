package com.hsbc.transaction.exception

import com.hsbc.transaction.exception.TransactionNotFoundException
import spock.lang.Specification

class TransactionNotFoundExceptionSpec extends Specification {
    def "should create exception with message"() {
        when:
        def ex = new TransactionNotFoundException("msg")
        then:
        ex.message == "msg"
    }
} 