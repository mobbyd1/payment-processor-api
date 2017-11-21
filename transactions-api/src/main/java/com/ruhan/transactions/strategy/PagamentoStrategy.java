package com.ruhan.transactions.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Component("PAGAMENTO")
public class PagamentoStrategy implements OperationStrategy {

    @Override
    public void execute(Long accountId, Double amount) throws Throwable {

    }
}
