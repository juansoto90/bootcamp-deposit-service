package com.nttdata.deposit.service;

import com.nttdata.deposit.model.entity.Deposit;
import reactor.core.publisher.Mono;

public interface IDepositService {
    public Mono<Deposit> save(Deposit deposit);
    public Mono<Deposit> findById(String id);
}
