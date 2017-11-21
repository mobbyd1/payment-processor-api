package com.ruhan.accounts.dao;

import com.ruhan.accounts.model.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ruhandosreis on 19/11/17.
 */
public interface AccountDAO extends CrudRepository<Account, Long> {
}
