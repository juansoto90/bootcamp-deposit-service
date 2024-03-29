package com.nttdata.deposit.service.Impl;

import com.nttdata.deposit.model.entity.Account;
import com.nttdata.deposit.service.IAccountService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountServiceImpl implements IAccountService {

    private final WebClient.Builder webClientBuilder;
    private final String WEB_CLIENT_URL = "microservice.web.account";
    private final String BASE;

    public AccountServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        BASE = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Mono<Account> updateAmountAccount(Account account) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .post()
                .uri("/update-amount")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .retrieve()
                .bodyToMono(Account.class);
    }

    @Override
    public Mono<Account> findByAccountNumber(String accountNumber) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .get()
                .uri("/account-number/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToMono(Account.class);
    }
}
