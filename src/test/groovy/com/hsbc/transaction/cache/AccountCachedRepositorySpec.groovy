package com.hsbc.transaction.cache

import com.hsbc.transaction.entity.Account
import com.hsbc.transaction.repository.AccountRepository
import spock.lang.Specification

class AccountCachedRepositorySpec extends Specification {
    def originRepo = Mock(AccountRepository)
    def repo = new AccountCachedRepository(originRepo)

    def "should update account in cache"() {
        def acc = new Account()
        when:
        repo.updateAccount(acc)
        then:
        1 * originRepo.save(acc)
    }
} 