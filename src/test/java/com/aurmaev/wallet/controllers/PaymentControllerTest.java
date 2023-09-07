package com.aurmaev.wallet.controllers;

import com.aurmaev.wallet.controllers.dto.PaymentDto;
import com.aurmaev.wallet.repositories.AccountRepository;
import com.aurmaev.wallet.repositories.PaymentRepository;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void createPaymentTransferBalance() throws Exception {
        AccountEntity firstAccount = generateAccount(BigDecimal.ZERO);
        AccountEntity secondAccount = generateAccount(BigDecimal.TEN);
        PaymentDto paymentDto = generatePaymentDto(firstAccount.getId(), secondAccount.getId(), BigDecimal.TEN);

        mockMvc.perform(post("/payment")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(paymentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.amount").value(BigDecimal.TEN))
                .andExpect(jsonPath("$.paymentDate").isNotEmpty())
                .andExpect(jsonPath("$.receiverAccountId").value(firstAccount.getId()))
                .andExpect(jsonPath("$.senderAccountId").value(secondAccount.getId()));

        PaymentEntity paymentEntity = paymentRepository.findAllByReceiverAccountIdOrSenderAccountId(firstAccount.getId(), secondAccount.getId()).get(0);
        Assertions.assertEquals(0, paymentEntity.getAmount().compareTo(BigDecimal.TEN));
        Assertions.assertTrue(paymentEntity.getPaymentDate().isBefore(LocalDateTime.now()));
        Assertions.assertEquals(paymentEntity.getReceiverAccount().getId(), firstAccount.getId());
        Assertions.assertEquals(paymentEntity.getSenderAccount().getId(), secondAccount.getId());
        Assertions.assertEquals(0, paymentEntity.getReceiverAccount().getBalance().compareTo(BigDecimal.TEN));
        Assertions.assertEquals(0, paymentEntity.getSenderAccount().getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void createPaymentTransferBalanceNegativeBalance() throws Exception {
        AccountEntity firstAccount = generateAccount(BigDecimal.ZERO);
        AccountEntity secondAccount = generateAccount(BigDecimal.ZERO);
        PaymentDto paymentDto = generatePaymentDto(firstAccount.getId(), secondAccount.getId(), BigDecimal.TEN);

        mockMvc.perform(post("/payment")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(paymentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", startsWith("Rejected balance reduction. Balance got a negative value.")));
    }

    private AccountEntity generateAccount(BigDecimal balance) {
        AccountEntity account = new AccountEntity();
        account.setBalance(balance);
        return accountRepository.save(account);
    }

    private PaymentDto generatePaymentDto(Long receiverId, Long senderId, BigDecimal amount) {
        return new PaymentDto(null, amount, null, receiverId, senderId);
    }
}
