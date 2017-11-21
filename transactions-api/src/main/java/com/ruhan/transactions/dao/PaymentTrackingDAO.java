package com.ruhan.transactions.dao;

import com.ruhan.transactions.model.PaymentTracking;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public interface PaymentTrackingDAO extends CrudRepository<PaymentTracking, Long> {
}
