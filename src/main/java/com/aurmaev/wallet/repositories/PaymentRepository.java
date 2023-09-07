package com.aurmaev.wallet.repositories;

import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllByReceiverAccountIdOrSenderAccountId(Long receiverAccountId, Long senderAccountId);
}
