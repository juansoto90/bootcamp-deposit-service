package com.nttdata.deposit.service;

import com.nttdata.deposit.model.entity.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMovementService {
    public Mono<Movement> save(Movement movement);
    public Flux<Movement> findByMovementDateBetweenAndAccountNumber(String accountNumber);
}
