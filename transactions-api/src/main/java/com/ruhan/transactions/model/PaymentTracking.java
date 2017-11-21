package com.ruhan.transactions.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Entity
@Table(name = "PAYMENTS_TRACKING", schema = "transaction")
public class PaymentTracking {

    @Id
    @Column(name = "payment_tracking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "credit_transaction_id")
    private Long creditTransactionId;

    @Column(name = "debit_transaction_id")
    private Long debitTransactionId;

    @Column
    private Double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreditTransactionId() {
        return creditTransactionId;
    }

    public void setCreditTransactionId(Long creditTransactionId) {
        this.creditTransactionId = creditTransactionId;
    }

    public Long getDebitTransactionId() {
        return debitTransactionId;
    }

    public void setDebitTransactionId(Long debitTransactionId) {
        this.debitTransactionId = debitTransactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
