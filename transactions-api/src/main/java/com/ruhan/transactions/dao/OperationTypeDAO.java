package com.ruhan.transactions.dao;

import com.ruhan.transactions.model.OperationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public interface OperationTypeDAO extends CrudRepository<OperationType, Long> {

    @Query("SELECT o FROM OperationType o " +
            "WHERE o.operation = :description")
    OperationType findByDescription(@Param("description") String description);
}
