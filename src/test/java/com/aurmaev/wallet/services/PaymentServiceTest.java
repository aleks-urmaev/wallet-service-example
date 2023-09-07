package com.aurmaev.wallet.services;

import com.aurmaev.wallet.controllers.dto.PaymentDto;
import com.aurmaev.wallet.repositories.PaymentRepository;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private AccountService accountService;

    @Test
    public void createPayment() {
        PaymentDto paymentDto = generatePaymentDto();

        Mockito.when(accountService.updateAccountBalance(eq(paymentDto.receiverAccountId()), eq(paymentDto.amount())))
                .thenReturn(new AccountEntity());
        Mockito.when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(new PaymentEntity());

        paymentService.createPayment(paymentDto);

        Mockito.verify(accountService).updateAccountBalance(eq(paymentDto.receiverAccountId()), eq(paymentDto.amount()));
        Mockito.verify(paymentRepository).save(any(PaymentEntity.class));
        Mockito.verifyNoMoreInteractions(accountService, paymentRepository);
    }

    private PaymentDto generatePaymentDto() {
        return new PaymentDto(null, BigDecimal.ONE, null, 1L, null);
    }
}
