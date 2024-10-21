package com.revolut.io.domain.transfer.model;

import java.math.BigDecimal;

public record TransferModel(
        String sender,
        String receiver,
        BigDecimal amount
) {
}
