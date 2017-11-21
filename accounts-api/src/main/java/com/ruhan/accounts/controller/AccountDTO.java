package com.ruhan.accounts.controller;

import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 19/11/17.
 */
public class AccountDTO {

    private Long id;
    private Double availableCreditLimit;
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
