package com.hsbc.transaction.repository;

import com.hsbc.transaction.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, String> {

}
