package com.revolut.io.domain.account.ports.inbound;

import com.revolut.io.domain.account.model.AccountModel;

import java.util.List;

public interface AccountService {
    void update(String accountId, AccountModel model);
    AccountModel getById(String accountId);
    List<AccountModel> getAll();
}
