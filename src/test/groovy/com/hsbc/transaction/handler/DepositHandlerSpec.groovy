package com.hsbc.transaction.handler

import com.hsbc.transaction.cache.AccountCachedRepository
import com.hsbc.transaction.cache.TransactionCachedRepository
import com.hsbc.transaction.entity.Account
import com.hsbc.transaction.exception.TransactionNotSupported
import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionType
import com.hsbc.transaction.model.TransactionStatus
import spock.lang.Specification

class DepositHandlerSpec extends Specification {
    def accountRepo = Mock(AccountCachedRepository)
    def transactionRepo = Mock(TransactionCachedRepository)
    def handler = new DepositHandler(accountRepo, transactionRepo)

    def "should handle DEPOSIT type"() {
        expect:
        handler.shouldHandle(TransactionType.DEPOSIT)
        !handler.shouldHandle(TransactionType.WITHDRAWAL)
    }

    def "initializeTransaction throws if creditAccountId is null"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> null
            transactionId() >> "tid"
        }
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction throws if credit account not active"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> "cid"
            transactionId() >> "tid"
        }
        def acc = Mock(Account) { isActive() >> false }
        accountRepo.getAccount("cid") >> acc
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction returns transaction if valid"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> "cid"
            transactionId() >> "tid"
            amount() >> 100
            description() >> "desc"
            transactionType() >> TransactionType.DEPOSIT
        }
        def acc = Mock(Account) {
            isActive() >> true
        }
        accountRepo.getAccount("cid") >> acc
        when:
        def tx = handler.initializeTransaction(req)
        then:
        tx.id == "tid"
        tx.amount == 100
        tx.creditAccountId == acc
        tx.status == TransactionStatus.PROCESSING.code
        tx.description == "desc"
        tx.type == TransactionType.DEPOSIT.code
    }
} 