package com.ruhan.transactions.dao;

import com.ruhan.transactions.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public interface TransactionDAO extends CrudRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.balance < 0.0 AND t.accountId = :accountId " +
            "ORDER BY t.operationType.chargeOrder, eventDate")
    List<Transaction> getPendingTransactions(@Param("accountId") Long accountId);
}
