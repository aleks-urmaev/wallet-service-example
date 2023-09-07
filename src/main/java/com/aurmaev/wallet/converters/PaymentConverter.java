package com.aurmaev.wallet.converters;

import com.aurmaev.wallet.controllers.dto.PaymentDto;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentConverter {

    public PaymentDto convert(PaymentEntity entity) {
        return new PaymentDto(
                entity.getId(),
                entity.getAmount(),
                entity.getPaymentDate(),
                entity.getReceiverAccount().getId(),
                Optional.ofNullable(entity.getSenderAccount())
                        .map(AccountEntity::getId)
                        .orElse(null));
    }

    public List<PaymentDto> convert(List<PaymentEntity> entities) {
        return entities.stream()
                .map(this::convert)
                .toList();
    }
}
