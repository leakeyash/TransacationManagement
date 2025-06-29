package com.hsbc.transaction

import com.hsbc.transaction.entity.Transaction
import com.hsbc.transaction.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class TransactionRepositorySpec extends Specification {
    @Autowired
    TransactionRepository repo

    def "save and find transaction"() {
        given:
        def tx = new Transaction()
        tx.id = "id"
        tx.amount = 100.0
        tx.status = "SUCCESS"
        when:
        def saved = repo.save(tx)
        def found = repo.findById(saved.id).orElse(null)
        then:
        found != null
        found.amount == 100.0
        found.status == "SUCCESS"
    }
} 