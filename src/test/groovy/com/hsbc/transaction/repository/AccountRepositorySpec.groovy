package com.hsbc.transaction

import com.hsbc.transaction.entity.Account
import com.hsbc.transaction.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class AccountRepositorySpec extends Specification {
    @Autowired
    AccountRepository repo

    def "save and find account"() {
        given:
        def acc = new Account()
        acc.balance = 100.0
        acc.active = true
        when:
        def saved = repo.save(acc)
        def found = repo.findById(saved.id).orElse(null)
        then:
        found != null
        found.balance == 100.0
        found.active
    }
} 