package com.ruhan.accounts.dlo;

import com.ruhan.accounts.model.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ruhandosreis on 19/11/17.
 */
public interface AccountDLO {

    Account getById(Long id );
    List<Account> getAll();
    void updateLimits(Long id, Double availableCreditLimit, Double availableWithDrawLimit);
}
