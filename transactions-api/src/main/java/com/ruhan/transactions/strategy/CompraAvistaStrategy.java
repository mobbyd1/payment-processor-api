package com.ruhan.transactions.strategy;

import com.ruhan.transactions.dlo.AccountDLO;
import com.ruhan.transactions.model.OperationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Component("COMPRA_A_VISTA")
public class CompraAvistaStrategy implements OperationStrategy {

    @Autowired
    private AccountDLO accountDLO;

    @Override
    public void execute(Long accountId, Double amount) throws Throwable {
        accountDLO.updateLimits( accountId, 0d, amount );
    }
}
