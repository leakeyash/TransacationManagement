package com.hsbc.transaction.handler

import com.hsbc.transaction.cache.AccountCachedRepository
import com.hsbc.transaction.cache.TransactionCachedRepository
import com.hsbc.transaction.entity.Account
import com.hsbc.transaction.entity.Transaction
import com.hsbc.transaction.exception.TransactionNotSupported
import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionType
import com.hsbc.transaction.model.TransactionStatus
import spock.lang.Specification

class WithdrawalHandlerSpec extends Specification {
    def accountRepo = Mock(AccountCachedRepository)
    def transactionRepo = Mock(TransactionCachedRepository)
    def handler = new WithdrawalHandler(accountRepo, transactionRepo)

    def "should handle WITHDRAWAL type"() {
        expect:
        handler.shouldHandle(TransactionType.WITHDRAWAL)
        !handler.shouldHandle(TransactionType.DEPOSIT)
    }

    def "initializeTransaction throws if debitAccountId is null"() {
        given:
        def req = Mock(TransactionRequest) {
            debitAccountId() >> null
            transactionId() >> "tid"
        }
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction throws if debit account not active"() {
        given:
        def req = Mock(TransactionRequest) {
            debitAccountId() >> "did"
            transactionId() >> "tid"
        }
        def acc = Mock(Account) { isActive() >> false }
        accountRepo.getAccount("did") >> acc
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction throws if insufficient balance"() {
        given:
        def req = Mock(TransactionRequest) {
            debitAccountId() >> "did"
            transactionId() >> "tid"
            amount() >> 200
        }
        def acc = Mock(Account) {
            isActive() >> true
            getBalance() >> 100
        }
        accountRepo.getAccount("did") >> acc
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction returns transaction if valid"() {
        given:
        def req = Mock(TransactionRequest) {
            debitAccountId() >> "did"
            transactionId() >> "tid"
            amount() >> 100
            description() >> "desc"
            transactionType() >> TransactionType.WITHDRAWAL
        }
        def acc = Mock(Account) {
            isActive() >> true
            getBalance() >> 200
        }
        accountRepo.getAccount("did") >> acc
        when:
        def tx = handler.initializeTransaction(req)
        then:
        tx.id == "tid"
        tx.amount == 100
        tx.debitAccountId == acc
        tx.status == TransactionStatus.PROCESSING.code
        tx.description == "desc"
        tx.type == TransactionType.WITHDRAWAL.code
    }
} 