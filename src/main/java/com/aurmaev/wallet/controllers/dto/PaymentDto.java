package com.aurmaev.wallet.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto (
        Long id,
        BigDecimal amount,
        LocalDateTime paymentDate,
        Long receiverAccountId,
        Long senderAccountId
) {}
