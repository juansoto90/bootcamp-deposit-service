package com.nttdata.deposit.service;

import com.nttdata.deposit.model.entity.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
    public Mono<Account> updateAmountAccount(Account account);
    public Mono<Account> findByAccountNumber(String accountNumber);
}
