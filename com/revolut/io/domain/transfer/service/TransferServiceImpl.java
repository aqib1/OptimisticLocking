package com.revolut.io.domain.transfer.service;

import com.revolut.io.domain.account.ports.inbound.AccountService;
import com.revolut.io.domain.transfer.exception.InvalidTransactionException;
import com.revolut.io.domain.transfer.model.TransferModel;
import com.revolut.io.domain.transfer.ports.inbound.TransferService;

import java.math.BigDecimal;
import java.util.concurrent.locks.StampedLock;

public class TransferServiceImpl implements TransferService {
    private final StampedLock optimisticLock;
    private final AccountService accountService;

    public TransferServiceImpl(
            StampedLock optimisticLock,
            AccountService accountService
    ) {
        this.optimisticLock = optimisticLock;
        this.accountService = accountService;
    }

    @Override
    public TransferModel transfer(TransferModel model) {
        var writeStamp = this.optimisticLock.writeLock();
        try {
            var senderAccount = this.accountService.getById(model.sender());
            var receiverAccount = this.accountService.getById(model.receiver());

            var senderUpdatedValue = senderAccount.getAmount().subtract(model.amount());
            if(senderUpdatedValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidTransactionException("Sender does not have enough balance");
            }

            receiverAccount.setAmount(receiverAccount.getAmount().add(model.amount()));
            senderAccount.setAmount(senderUpdatedValue);

            accountService.update(model.sender(), senderAccount);
            accountService.update(model.receiver(), receiverAccount);

            return model;
        } finally {
            this.optimisticLock.unlockWrite(writeStamp);
        }
    }
}
