package com.nttdata.deposit.service.Impl;

import com.nttdata.deposit.model.entity.Association;
import com.nttdata.deposit.service.IAssociationService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AssociationServiceImpl implements IAssociationService {

    private final WebClient.Builder webClientBuilder;
    private final String WEB_CLIENT_URL = "microservice.web.association";
    private final String BASE;

    public AssociationServiceImpl(WebClient.Builder webClientBuilder, Environment env) {
        this.webClientBuilder = webClientBuilder;
        BASE = env.getProperty(WEB_CLIENT_URL);
    }

    @Override
    public Mono<Association> findByAccountNumberAndStatus(String accountNumber, String status) {
        return webClientBuilder
                .baseUrl(BASE)
                .build()
                .get()
                .uri("/{accountNumber}/{status}", accountNumber, status)
                .retrieve()
                .bodyToMono(Association.class);
    }
}
