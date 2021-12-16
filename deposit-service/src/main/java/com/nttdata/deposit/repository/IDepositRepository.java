package com.nttdata.deposit.repository;

import com.nttdata.deposit.model.entity.Deposit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IDepositRepository extends ReactiveMongoRepository<Deposit, String> {
}
