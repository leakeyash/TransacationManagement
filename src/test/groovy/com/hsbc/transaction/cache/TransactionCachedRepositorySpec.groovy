package com.hsbc.transaction.cache

import com.hsbc.transaction.entity.Transaction
import com.hsbc.transaction.repository.TransactionRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class TransactionCachedRepositorySpec extends Specification {
    def repo = Mock(TransactionRepository)
    def cachedRepo = new TransactionCachedRepository(repo)

    def "should get transaction from repository"() {
        def tx = new Transaction()
        repo.findById("tid") >> Optional.of(tx)
        expect:
        cachedRepo.getTransaction("tid") == tx
    }

    def "should return null if transaction not found"() {
        repo.findById("tid") >> Optional.empty()
        expect:
        cachedRepo.getTransaction("tid") == null
    }

    def "should get transactions as page"() {
        def txs = [new Transaction(id: "1"), new Transaction(id: "2")]
        def page = new PageImpl(txs, PageRequest.of(0, 2), 2)
        repo.findAll(_) >> page
        expect:
        cachedRepo.getTransactions(PageRequest.of(0, 2)).content == txs
    }

    def "should update transaction"() {
        def tx = new Transaction()
        repo.save(tx) >> tx
        expect:
        cachedRepo.updateTransaction(tx) == tx
    }

    def "should delete transaction"() {
        def tx = new Transaction()
        when:
        cachedRepo.deleteTransaction(tx)
        then:
        1 * repo.delete(tx)
    }
} 