package com.hsbc.transaction.model

import spock.lang.Specification

class TransactionStatusSpec extends Specification {

    def "should find by code"() {
        expect:
        TransactionStatus.fromCode("SUCCESS") == TransactionStatus.SUCCESS
        TransactionStatus.fromCode("FAILED") == TransactionStatus.FAILED
        TransactionStatus.fromCode("PROCESSING") == TransactionStatus.PROCESSING
    }
} 