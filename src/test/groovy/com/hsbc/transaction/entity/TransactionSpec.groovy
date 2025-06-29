package com.hsbc.transaction.entity

import spock.lang.Specification

class TransactionSpec extends Specification {
    def "should create Transaction and access properties"() {
        given:
        def tx = new Transaction()
        tx.setId("tid")
        tx.setAmount(100)
        tx.setDescription("desc")
        tx.setStatus("SUCCESS")
        tx.setMessage("ok")
        when:
        tx.setStatus("FAILED")
        then:
        tx.getId() == "tid"
        tx.getAmount() == 100
        tx.getDescription() == "desc"
        tx.getStatus() == "FAILED"
        tx.getMessage() == "ok"
    }
} 