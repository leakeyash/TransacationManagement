package com.hsbc.transaction.handler

import com.hsbc.transaction.cache.AccountCachedRepository
import com.hsbc.transaction.cache.TransactionCachedRepository
import com.hsbc.transaction.entity.Account
import com.hsbc.transaction.exception.TransactionNotSupported
import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionType
import com.hsbc.transaction.model.TransactionStatus
import spock.lang.Specification

class TransferHandlerSpec extends Specification {
    def accountRepo = Mock(AccountCachedRepository)
    def transactionRepo = Mock(TransactionCachedRepository)
    def handler = new TransferHandler(accountRepo, transactionRepo)

    def "should handle TRANSFER type"() {
        expect:
        handler.shouldHandle(TransactionType.TRANSFER)
        !handler.shouldHandle(TransactionType.DEPOSIT)
    }

    def "initializeTransaction throws if credit or debit account is null"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> null
            debitAccountId() >> null
            transactionId() >> "tid"
        }
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction throws if credit and debit account are the same"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> "id1"
            debitAccountId() >> "id1"
            transactionId() >> "tid"
        }
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction throws if account not exist or not active"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> "cid"
            debitAccountId() >> "did"
            transactionId() >> "tid"
        }
        def acc1 = Mock(Account) { isActive() >> false }
        def acc2 = Mock(Account) { isActive() >> true }
        accountRepo.getAccount("did") >> acc1
        accountRepo.getAccount("cid") >> acc2
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction throws if insufficient balance in debit account"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> "cid"
            debitAccountId() >> "did"
            transactionId() >> "tid"
            amount() >> 200
        }
        def acc1 = Mock(Account) {
            isActive() >> true
            getBalance() >> 100
        }
        def acc2 = Mock(Account) { isActive() >> true }
        accountRepo.getAccount("did") >> acc1
        accountRepo.getAccount("cid") >> acc2
        when:
        handler.initializeTransaction(req)
        then:
        thrown(TransactionNotSupported)
    }

    def "initializeTransaction returns transaction if valid"() {
        given:
        def req = Mock(TransactionRequest) {
            creditAccountId() >> "cid"
            debitAccountId() >> "did"
            transactionId() >> "tid"
            amount() >> 100
            description() >> "desc"
            transactionType() >> TransactionType.TRANSFER
        }
        def acc1 = Mock(Account) {
            isActive() >> true
            getBalance() >> 200
        }
        def acc2 = Mock(Account) { isActive() >> true }
        accountRepo.getAccount("did") >> acc1
        accountRepo.getAccount("cid") >> acc2
        when:
        def tx = handler.initializeTransaction(req)
        then:
        tx.id == "tid"
        tx.amount == 100
        tx.debitAccountId == acc1
        tx.creditAccountId == acc2
        tx.status == TransactionStatus.PROCESSING.code
        tx.description == "desc"
        tx.type == TransactionType.TRANSFER.code
    }
} 