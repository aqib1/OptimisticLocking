package com.revolut.io.domain.transfer.ports.inbound;

import com.revolut.io.domain.transfer.model.TransferModel;

public interface TransferService {
    TransferModel transfer(TransferModel model);
}
