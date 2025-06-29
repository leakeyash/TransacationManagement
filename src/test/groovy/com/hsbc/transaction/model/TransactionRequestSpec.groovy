package com.hsbc.transaction.model

import spock.lang.Specification

class TransactionRequestSpec extends Specification {
    def "should create TransactionRequest and access properties"() {
        given:
        def req = new TransactionRequest("tid", BigDecimal.ONE, "debit",
                "credit", "DEPOSIT", "desc")
        expect:
        req.transactionId() == "tid"
        req.amount() == 1
        req.debitAccountId() == "debit"
        req.creditAccountId() == "credit"
        req.description() == "desc"
        req.transactionType() == "DEPOSIT"
    }
} 