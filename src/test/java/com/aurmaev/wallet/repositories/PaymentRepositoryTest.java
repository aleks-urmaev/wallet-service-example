package com.aurmaev.wallet.repositories;

import com.aurmaev.wallet.repositories.entities.AccountEntity;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class PaymentRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void findAllByReceiverAccountIdOrSenderAccountId() {
        AccountEntity accountEntity = accountRepository.save(new AccountEntity());
        generatePayment(accountEntity, null);
        generatePayment(null, accountEntity);
        generatePayment(accountEntity, null);

        List<PaymentEntity> paymentEntities =
                paymentRepository.findAllByReceiverAccountIdOrSenderAccountId(accountEntity.getId(), accountEntity.getId());
        Assertions.assertEquals(paymentEntities.size(), 3);
    }



    private PaymentEntity generatePayment(AccountEntity receiver, AccountEntity sender) {
        PaymentEntity payment = new PaymentEntity();
        payment.setReceiverAccount(receiver);
        payment.setSenderAccount(sender);
        payment.setAmount(BigDecimal.TEN);
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}
