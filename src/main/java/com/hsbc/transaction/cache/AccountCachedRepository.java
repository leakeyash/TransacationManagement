package com.hsbc.transaction.cache;

import com.hsbc.transaction.entity.Account;
import com.hsbc.transaction.repository.AccountRepository;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "accountCache")
public class AccountCachedRepository {
  private final AccountRepository accountRepository;

  public AccountCachedRepository(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Cacheable(key = "#accountId")
  public Account getAccount(String accountId) {
    Optional<Account> account = accountRepository.findById(accountId);
    return account.orElse(null);
  }

  @CacheEvict(key = "#account.id", beforeInvocation = true)
  @CachePut(key = "#account.id")
  public Account updateAccount(Account account) {
    return accountRepository.save(account);
  }
}
