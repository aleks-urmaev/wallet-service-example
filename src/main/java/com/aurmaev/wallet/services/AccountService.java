package com.aurmaev.wallet.services;

import com.aurmaev.wallet.exceptions.BadRequestException;
import com.aurmaev.wallet.exceptions.DataNotFoundException;
import com.aurmaev.wallet.repositories.AccountRepository;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountEntity getAccount(Long accountId) {
        AccountEntity accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new DataNotFoundException(
                        MessageFormat.format(DataNotFoundException.ACCOUNT_NOT_FOUND, accountId)));
        logger.info("Found requested account with ID: {}", accountEntity.getId());
        return accountEntity;
    }

    public AccountEntity createAccount() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(BigDecimal.ZERO);
        accountEntity = accountRepository.save(accountEntity);
        logger.info("Created account record with ID: {}", accountEntity.getId());
        return accountEntity;
    }

    public AccountEntity updateAccountBalance(Long accountId, BigDecimal amount) {
        AccountEntity accountEntity;
        if (accountId == null) {
            accountEntity = createAccount();
        } else {
            accountEntity = getAccount(accountId);
        }
        return updateAccountBalance(accountEntity, amount);
    }

    public BigDecimal getAccountBalance(long accountId) {
        AccountEntity accountEntity = getAccount(accountId);
        return accountEntity.getBalance();
    }

    private AccountEntity updateAccountBalance(AccountEntity accountEntity, BigDecimal amount) {
        BigDecimal balance = accountEntity.getBalance().add(amount);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(MessageFormat.format(BadRequestException.BALANCE_NEGATIVE_VALUE,
                    accountEntity.getId(), accountEntity.getBalance(), amount));
        }
        accountEntity.setBalance(balance);
        accountEntity = accountRepository.save(accountEntity);
        logger.info("Updated balance value for account with ID: {}", accountEntity.getId());
        return accountEntity;
    }
}
