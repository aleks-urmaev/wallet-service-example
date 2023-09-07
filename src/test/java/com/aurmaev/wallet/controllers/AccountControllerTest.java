package com.aurmaev.wallet.controllers;

import com.aurmaev.wallet.repositories.AccountRepository;
import com.aurmaev.wallet.repositories.PaymentRepository;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account not found";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void createAccount() throws Exception {
        mockMvc.perform(post("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(BigDecimal.ZERO));
    }

    @Test
    public void getBalance() throws Exception {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(BigDecimal.TEN);
        accountEntity = accountRepository.save(accountEntity);

        mockMvc.perform(get("/account/{accountId}/balance", accountEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(BigDecimal.TEN.doubleValue()));
    }

    @Test
    public void getBalanceNotFound() throws Exception {
        mockMvc.perform(get("/account/{accountId}/balance", RandomUtils.nextInt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", startsWith(ACCOUNT_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void getPayments() throws Exception {
        AccountEntity account = accountRepository.save(new AccountEntity());
        generatePayment(account);
        generatePayment(account);

        mockMvc.perform(get("/account/{accountId}/payments", account.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].amount").value(BigDecimal.TEN.doubleValue()))
                .andExpect(jsonPath("$[0].paymentDate").isNotEmpty())
                .andExpect(jsonPath("$[0].receiverAccountId").value(account.getId()))
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].amount").value(BigDecimal.TEN.doubleValue()))
                .andExpect(jsonPath("$[1].paymentDate").isNotEmpty())
                .andExpect(jsonPath("$[1].receiverAccountId").value(account.getId()));
    }

    private PaymentEntity generatePayment(AccountEntity account) {
        PaymentEntity payment = new PaymentEntity();
        payment.setReceiverAccount(account);
        payment.setAmount(BigDecimal.TEN);
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}
