package com.revolut.io.domain.account.service;

import com.revolut.io.domain.account.model.AccountModel;
import com.revolut.io.domain.account.ports.inbound.AccountService;
import com.revolut.io.domain.account.ports.outbound.AccountRepository;

import java.util.List;
import java.util.concurrent.locks.StampedLock;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final StampedLock lock;

    public AccountServiceImpl(
            StampedLock lock,
            AccountRepository repository
    ) {
        this.lock = lock;
        this.repository = repository;
    }

    @Override
    public void update(String accountId, AccountModel model) {
        var stamp = lock.writeLock();
        try {
            repository.update(accountId, model);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    @Override
    public AccountModel getById(String accountId) {
        var stamp = lock.tryOptimisticRead();
        if (lock.validate(stamp))
            return repository.getById(accountId);
        // require lock
        stamp = lock.readLock();
        try {
            return repository.getById(accountId);
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public List<AccountModel> getAll() {
        var stamp = lock.tryOptimisticRead();
        if (lock.validate(stamp))
            return repository.getAll();
        // require lock
        stamp = lock.readLock();
        try {
            return repository.getAll();
        } finally {
            lock.unlockRead(stamp);
        }
    }
}
