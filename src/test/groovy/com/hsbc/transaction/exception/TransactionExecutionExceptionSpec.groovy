package com.hsbc.transaction.exception

import com.hsbc.transaction.exception.TransactionExecutionException
import spock.lang.Specification

class TransactionExecutionExceptionSpec extends Specification {
    def "should create exception with message"() {
        when:
        def ex = new TransactionExecutionException("msg")
        then:
        ex.message == "msg"
    }
} 