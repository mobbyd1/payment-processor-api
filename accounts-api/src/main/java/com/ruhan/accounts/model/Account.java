package com.ruhan.accounts.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 19/11/17.
 */
@Entity
@Table(name="ACCOUNTS", schema = "account")
public class Account {

    @Id
    @Column
    private Long id;

    @Column(name = "available_credit_limit")
    private Double availableCreditLimit;

    @Column(name = "available_withdraw_limit")
    private Double availableWithdrawLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    public void setAvailableCreditLimit(Double availableCreditLimit) {
        this.availableCreditLimit = availableCreditLimit;
    }

    public Double getAvailableWithdrawLimit() {
        return availableWithdrawLimit;
    }

    public void setAvailableWithdrawLimit(Double availableWithdrawLimit) {
        this.availableWithdrawLimit = availableWithdrawLimit;
    }
}
