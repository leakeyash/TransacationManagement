package com.hsbc.transaction.handler

import com.hsbc.transaction.cache.TransactionCachedRepository
import com.hsbc.transaction.entity.Transaction
import com.hsbc.transaction.exception.TransactionNotSupported
import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionResponse
import com.hsbc.transaction.model.TransactionType
import spock.lang.Specification

class TransactionHandleProxySpec extends Specification {
    def transactionRepo = Mock(TransactionCachedRepository)
    def handler1 = Mock(TransactionHandler)
    def handler2 = Mock(TransactionHandler)
    def proxy = new TransactionHandleProxy([handler1, handler2], transactionRepo)

    def "should delegate to correct handler"() {
        given:
        def req = new TransactionRequest("TXN1", BigDecimal.ONE, "debit",
                "credit", TransactionType.DEPOSIT.code, "TEST")
        handler1.shouldHandle(TransactionType.DEPOSIT) >> false
        handler2.shouldHandle(TransactionType.DEPOSIT) >> true
        def tx = Mock(Transaction)
        handler2.initializeTransaction(req) >> tx
        handler2.handle(tx) >> _
        transactionRepo.updateTransaction(tx) >> _
        when:
        proxy.handleTransactionRequest(req)
        then:
        1 * handler2.handle(tx)
    }

    def "should throw if no handler found"() {
        given:
        def req = new TransactionRequest("TXN1", BigDecimal.ONE, "debit",
                "credit", "unknown", "TEST")
        handler1.shouldHandle(_) >> false
        handler2.shouldHandle(_) >> false
        when:
        proxy.handleTransactionRequest(req)
        then:
        thrown(IllegalArgumentException)
    }
} 