package com.hsbc.transaction.service

import com.hsbc.transaction.cache.TransactionCachedRepository
import com.hsbc.transaction.entity.Transaction
import com.hsbc.transaction.exception.DuplicateTransactionException
import com.hsbc.transaction.exception.TransactionNotFoundException
import com.hsbc.transaction.handler.TransactionHandleProxy
import com.hsbc.transaction.model.PageResponse
import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionResponse
import com.hsbc.transaction.model.TransactionStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime

class TransactionServiceImplSpec extends Specification {
    def transactionRepo = Mock(TransactionCachedRepository)
    def proxy = Mock(TransactionHandleProxy)
    def service = new TransactionServiceImpl(transactionRepo, proxy)

    def "create throws if transaction exists"() {
        given:
        def req = new TransactionRequest("tid", BigDecimal.ONE,"debit","credit","test","test")
        transactionRepo.getTransaction("tid") >> new Transaction()
        when:
        service.create(req)
        then:
        thrown(DuplicateTransactionException)
    }

    def "create delegates to proxy if not exists"() {
        given:
        def req = new TransactionRequest("tid", BigDecimal.ONE,"debit","credit","test","test")
        transactionRepo.getTransaction("tid") >> null
        proxy.handleTransactionRequest(req) >> mockTransactionResponse()
        when:
        def resp = service.create(req)
        then:
        resp instanceof TransactionResponse
    }

    def "update throws if transaction not found"() {
        given:
        def req = new TransactionRequest("xy", BigDecimal.ONE,"debit","credit","test","test")
        transactionRepo.getTransaction("tid") >> null
        when:
        service.update("tid", req)
        then:
        thrown(TransactionNotFoundException)
    }

    def "update throws if transaction not failed"() {
        given:
        def req = new TransactionRequest("xy", BigDecimal.ONE,"debit","credit","test","test")
        def tx = Mock(Transaction) { getStatus() >> TransactionStatus.SUCCESS.code }
        transactionRepo.getTransaction("tid") >> tx
        when:
        service.update("tid", req)
        then:
        thrown(TransactionNotFoundException)
    }

    def "update delegates to proxy if failed"() {
        given:
        def req = new TransactionRequest("xy", BigDecimal.ONE,"debit","credit","test","test")
        def tx = Mock(Transaction) { getStatus() >> TransactionStatus.FAILED.code }
        transactionRepo.getTransaction("tid") >> tx
        proxy.handleTransactionRequest(req) >> mockTransactionResponse()
        when:
        def resp = service.update("tid", req)
        then:
        resp instanceof TransactionResponse
    }

    def "delete throws if transaction not found"() {
        transactionRepo.getTransaction("tid") >> null
        when:
        service.delete("tid")
        then:
        thrown(TransactionNotFoundException)
    }

    def "delete calls repo if found"() {
        def tx = Mock(Transaction)
        transactionRepo.getTransaction("tid") >> tx
        when:
        service.delete("tid")
        then:
        1 * transactionRepo.deleteTransaction(tx)
    }

    def "getById throws if not found"() {
        transactionRepo.getTransaction("tid") >> null
        when:
        service.getById("tid")
        then:
        thrown(TransactionNotFoundException)
    }

    def "getById returns response if found"() {
        def tx = Mock(Transaction)
        transactionRepo.getTransaction("tid") >> tx
        when:
        def resp = service.getById("tid")
        then:
        resp instanceof TransactionResponse
    }

    def "getAllPaged returns page response"() {
        def tx = Mock(Transaction)
        def page = new PageImpl([tx], PageRequest.of(0, 1), 1)
        transactionRepo.getTransactions(_) >> page
        when:
        def resp = service.getAllPaged(PageRequest.of(0, 1))
        then:
        resp instanceof PageResponse
    }

    def mockTransactionResponse() {
        return new TransactionResponse("1","d","c","t",null,"d", LocalDateTime.now(),TransactionStatus.FAILED.code,"m")
    }
} 