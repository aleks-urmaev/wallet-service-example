package com.aurmaev.wallet.services;

import com.aurmaev.wallet.controllers.dto.PaymentDto;
import com.aurmaev.wallet.repositories.PaymentRepository;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final AccountService accountService;
    private final PaymentRepository paymentRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);


    @Autowired
    public PaymentService(AccountService accountService, PaymentRepository paymentRepository) {
        this.accountService = accountService;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentEntity createPayment(PaymentDto paymentDto) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(paymentDto.amount());
        paymentEntity.setPaymentDate(LocalDateTime.now());
        Long senderAccountId = paymentDto.senderAccountId();
        if (senderAccountId != null) {
            paymentEntity.setSenderAccount(
                    accountService.updateAccountBalance(senderAccountId, paymentDto.amount().negate()));
        }
        paymentEntity.setReceiverAccount(
                accountService.updateAccountBalance(paymentDto.receiverAccountId(), paymentDto.amount()));
        paymentEntity = paymentRepository.save(paymentEntity);
        logger.info("Created payment record with ID: {}", paymentEntity.getId());
        return paymentEntity;
    }

    public List<PaymentEntity> getPaymentsByAccountId(Long accountId) {
        logger.info("Requested payment history for account with ID: {}", accountId);
        return paymentRepository.findAllByReceiverAccountIdOrSenderAccountId(accountId, accountId);
    }
}
