package com.aurmaev.wallet.controllers;

import com.aurmaev.wallet.controllers.dto.AccountDto;
import com.aurmaev.wallet.controllers.dto.PaymentDto;
import com.aurmaev.wallet.converters.AccountConverter;
import com.aurmaev.wallet.converters.PaymentConverter;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import com.aurmaev.wallet.services.AccountService;
import com.aurmaev.wallet.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Account", description = "Account management API")
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final PaymentService paymentService;
    private final AccountConverter accountConverter;
    private final PaymentConverter paymentConverter;

    @Autowired
    public AccountController(AccountService accountService,
                             PaymentService paymentService,
                             AccountConverter accountConverter,
                             PaymentConverter paymentConverter) {
        this.accountService = accountService;
        this.paymentService = paymentService;
        this.accountConverter = accountConverter;
        this.paymentConverter = paymentConverter;
    }

    @Operation(summary = "Create account", description = "Creating a new account in the system")
    @PostMapping
    public AccountDto createAccount() {
        AccountEntity accountEntity = accountService.createAccount();
        return accountConverter.convert(accountEntity);
    }

    @Operation(summary = "Get balance", description = "Get current balance value for an existing account")
    @GetMapping("/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable long accountId) {
        return accountService.getAccountBalance(accountId);
    }

    @Operation(summary = "Get payments", description = "Get payment history for an existing account")
    @GetMapping("/{accountId}/payments")
    public List<PaymentDto> getPayments(@PathVariable long accountId) {
        List<PaymentEntity> paymentEntities = paymentService.getPaymentsByAccountId(accountId);
        return paymentConverter.convert(paymentEntities);
    }
}
