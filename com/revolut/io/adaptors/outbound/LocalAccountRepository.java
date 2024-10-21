package com.revolut.io.adaptors.outbound;

import com.revolut.io.domain.account.model.AccountModel;
import com.revolut.io.domain.account.ports.outbound.AccountRepository;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalAccountRepository implements AccountRepository {
    private final Map<String, AccountModel> accounts;

    public LocalAccountRepository() {
        this.accounts = new ConcurrentHashMap<>(
                Map.of(
                        "ab11", new AccountModel(
                                "ab11",
                                BigDecimal.valueOf(1000),
                                Currency.getInstance("USD")
                        ),
                        "ab12", new AccountModel(
                                "ab12",
                                BigDecimal.valueOf(50),
                                Currency.getInstance("USD")
                        )
                )
        );
    }

    @Override
    public AccountModel update(String id, AccountModel model) {
        return this.accounts.put(id, model);
    }

    @Override
    public AccountModel save(AccountModel model) {
        return accounts.putIfAbsent(model.getAccountId(), model);
    }

    @Override
    public AccountModel getById(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public List<AccountModel> getAll() {
        return accounts.values().stream().toList();
    }
}
