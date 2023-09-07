package com.aurmaev.wallet.services;

import com.aurmaev.wallet.exceptions.BadRequestException;
import com.aurmaev.wallet.repositories.AccountRepository;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    @Test
    public void updateAccountBalance() {
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.TEN;
        AccountEntity account = generateAccount(BigDecimal.ZERO);

        Mockito.when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        AccountEntity accountEntity = accountService.updateAccountBalance(accountId, amount);
        Assertions.assertEquals(0, accountEntity.getBalance().compareTo(BigDecimal.TEN));

        Mockito.verify(accountRepository).findById(eq(accountId));
        Mockito.verify(accountRepository).save(account);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void updateAccountBalanceRejected() {
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.ZERO;
        AccountEntity account = generateAccount(BigDecimal.TEN.negate());

        Mockito.when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class,
                () -> accountService.updateAccountBalance(accountId, amount));
        Assertions.assertTrue(exception.getMessage().startsWith("Rejected balance reduction. Balance got a negative value."));

        Mockito.verify(accountRepository).findById(eq(accountId));
        Mockito.verify(accountRepository, Mockito.never()).save(account);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }

    private AccountEntity generateAccount(BigDecimal balance) {
        AccountEntity account = new AccountEntity();
        account.setBalance(balance);
        return account;
    }
}
