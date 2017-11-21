package com.ruhan.transactions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by ruhandosreis on 20/11/17.
 */
public class PaymentDTO {

    @JsonProperty("account_id")
    private Long accountId;

    private Double amount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
