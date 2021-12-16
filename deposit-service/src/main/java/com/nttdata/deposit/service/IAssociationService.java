package com.nttdata.deposit.service;

import com.nttdata.deposit.model.entity.Association;
import reactor.core.publisher.Mono;

public interface IAssociationService {
    public Mono<Association> findByAccountNumberAndStatus(String accountNumber, String status);
}
