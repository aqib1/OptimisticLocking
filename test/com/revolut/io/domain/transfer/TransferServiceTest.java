package com.revolut.io.domain.transfer;

import com.revolut.io.adaptors.outbound.LocalAccountRepository;
import com.revolut.io.domain.account.service.AccountServiceImpl;
import com.revolut.io.domain.transfer.model.TransferModel;
import com.revolut.io.domain.transfer.service.TransferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

public class TransferServiceTest {

    @Test
    public void transferMoney_WhenValidInputProvided_ShouldSuccess() {
        var accountRepository = new LocalAccountRepository();
        // stampedLock is non-reentrant
        var accountService = new AccountServiceImpl(new StampedLock(), accountRepository);
        var transferService = new TransferServiceImpl(new StampedLock(), accountService);


        // when transfer is made
        try(var fixedThreadPool = Executors.newFixedThreadPool(10)) {
            for(int i = 0; i < 10; i++) {
                fixedThreadPool.execute(() ->
                        transferService.transfer(new TransferModel("ab11", "ab12", BigDecimal.TEN)));
            }
        }


        // then check transfer
        // 1000 - 100 (10 thread and each is transferring 10)
        var sender = accountService.getById("ab11");
        Assertions.assertEquals(BigDecimal.valueOf(900), sender.getAmount());
        // 50 + 100 = 150
        var receiver = accountService.getById("ab12");
        Assertions.assertEquals(BigDecimal.valueOf(150), receiver.getAmount());
    }
}
