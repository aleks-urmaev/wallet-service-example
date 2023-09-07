package com.aurmaev.wallet.converters;

import com.aurmaev.wallet.controllers.dto.AccountDto;
import com.aurmaev.wallet.repositories.entities.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter {

    public AccountDto convert(AccountEntity entity) {
        return new AccountDto(entity.getId(), entity.getBalance());
    }
}
