package com.revolut.io.domain.account.model;

import java.math.BigDecimal;
import java.util.Currency;

public class AccountModel {
    private String accountId;
    private BigDecimal amount;
    private Currency currency;

    public AccountModel(String accountId, BigDecimal amount, Currency currency) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
