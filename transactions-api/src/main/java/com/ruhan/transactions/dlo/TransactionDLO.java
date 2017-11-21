package com.ruhan.transactions.dlo;

import com.ruhan.transactions.dto.PaymentDTO;
import com.ruhan.transactions.dto.TransactionDTO;
import com.ruhan.transactions.model.Transaction;
import java.util.List;

/**
 * Created by ruhandosreis on 20/11/17.
 */

public interface TransactionDLO {

    List<Transaction> getAllTransactions();
    Transaction getTransactionById( Long id );
    void registerPayment( PaymentDTO paymentDTO ) throws Throwable;
    void registerTransaction(TransactionDTO transactionDTO) throws Throwable;
}
