package com.ruhan.transactions.strategy;

import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public interface OperationStrategy {

    void execute( Long accountId, Double amount ) throws Throwable;
}
