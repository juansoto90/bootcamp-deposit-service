package com.nttdata.deposit.service.Impl;

import com.nttdata.deposit.model.entity.Deposit;
import com.nttdata.deposit.repository.IDepositRepository;
import com.nttdata.deposit.service.IDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements IDepositService {

    private final IDepositRepository repository;

    @Override
    public Mono<Deposit> save(Deposit deposit) {
        return repository.save(deposit);
    }

    @Override
    public Mono<Deposit> findById(String id) {
        return repository.findById(id);
    }
}
