package com.hsbc.transaction.model

import com.hsbc.transaction.entity.Transaction
import spock.lang.Specification

class TransactionResponseSpec extends Specification {
    def "should create TransactionResponse and access properties"() {
        given:
        def resp = new TransactionResponse("tid", "debit", "credit",
                "desc", null, "desc", null, "SUCCESS", null)
        expect:
        resp.transactionId() == "tid"
        resp.debitAccountId() == "debit"
        resp.creditAccountId() == "credit"
        resp.description() == "desc"
        resp.status() == "SUCCESS"
    }

    def "should create from Transaction entity"() {
        given:
        def tx = Mock(Transaction) {
            getId() >> "tid"
            getDebitAccountId() >> null
            getCreditAccountId() >> null
            getDescription() >> "desc"
            getStatus() >> "SUCCESS"
            getMessage() >> null
        }
        when:
        def resp = TransactionResponse.fromTransaction(tx)
        then:
        resp.transactionId() == "tid"
        resp.description() == "desc"
        resp.status() == "SUCCESS"
    }
} 