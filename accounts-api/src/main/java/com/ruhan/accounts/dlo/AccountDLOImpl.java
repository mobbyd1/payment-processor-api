package com.ruhan.accounts.dlo;

import com.ruhan.accounts.dao.AccountDAO;
import com.ruhan.accounts.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruhandosreis on 19/11/17.
 */
@Component
public class AccountDLOImpl implements AccountDLO {

    @Autowired
    AccountDAO accountDAO;

    @Override
    public Account getById(Long id) {
        final Account account = accountDAO.findOne(id);
        return account;
    }

    @Override
    public List<Account> getAll() {
        final List<Account> accounts = new ArrayList<>();
        final Iterable<Account> all = accountDAO.findAll();

        all.forEach( accounts::add );
        return accounts;
    }

    @Override
    public void updateLimits(Long id, Double availableCreditLimit, Double availableWithDrawLimit) {
        final Account account = getById(id);

        final Double availableCreditLimitSaved = account.getAvailableCreditLimit();
        final Double availableWithDrawLimitSaved = account.getAvailableWithdrawLimit();

        account.setAvailableCreditLimit( availableCreditLimitSaved + availableCreditLimit );
        account.setAvailableWithdrawLimit( availableWithDrawLimitSaved + availableWithDrawLimit );

        accountDAO.save( account );
    }
}
