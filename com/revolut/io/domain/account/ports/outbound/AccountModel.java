package com.revolut.io.domain.account.ports.outbound;

import com.revolut.io.domain.account.model.AccountModel;

import java.util.List;

public interface AccountRepository {
    AccountModel update(String id, AccountModel model);
    AccountModel save(AccountModel model);
    AccountModel getById(String id);
    List<AccountModel> getAll();
}
