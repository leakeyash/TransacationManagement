package com.hsbc.transaction.model


import spock.lang.Specification

class TransactionTypeSpec extends Specification {
    def "fromCode returns correct type"() {
        expect:
        TransactionType.fromCode("DEPOSIT") == TransactionType.DEPOSIT
        TransactionType.fromCode("WITHDRAWAL") == TransactionType.WITHDRAWAL
        TransactionType.fromCode("TRANSFER") == TransactionType.TRANSFER
    }

    def "fromCode throws for unknown"() {
        when:
        TransactionType.fromCode("XXX")
        then:
        thrown(IllegalArgumentException)
    }

    def "fromName returns correct type"() {
        expect:
        TransactionType.fromName("deposit") == TransactionType.DEPOSIT
        TransactionType.fromName("withdrawal") == TransactionType.WITHDRAWAL
        TransactionType.fromName("transfer") == TransactionType.TRANSFER
    }

    def "fromName throws for unknown"() {
        when:
        TransactionType.fromName("xxx")
        then:
        thrown(IllegalArgumentException)
    }

    def "toString returns code"() {
        expect:
        TransactionType.DEPOSIT.toString() == "DEPOSIT"
    }
} 