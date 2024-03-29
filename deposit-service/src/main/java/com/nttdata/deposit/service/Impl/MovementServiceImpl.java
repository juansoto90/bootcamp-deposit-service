package com.nttdata.deposit.service.Impl;

import com.nttdata.deposit.model.entity.Movement;
import com.nttdata.deposit.service.IMovementService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovementServiceImpl implements IMovementService {

    private final WebClient.Builder webClientBuilder;
    private final String WEB_CLIENT_URL = "microservice.web.movement";
    private final String BASE;


    public MovementServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        BASE = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Mono<Movement> save(Movement movement) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movement)
                .retrieve()
                .bodyToMono(Movement.class);
    }

    @Override
    public Flux<Movement> findByMovementDateBetweenAndAccountNumber(String accountNumber) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .get()
                .uri("/account-number/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToFlux(Movement.class);
    }
}
