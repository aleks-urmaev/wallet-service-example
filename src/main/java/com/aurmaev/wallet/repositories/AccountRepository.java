package com.aurmaev.wallet.repositories;

import com.aurmaev.wallet.repositories.entities.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
}
