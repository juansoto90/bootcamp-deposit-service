package com.nttdata.deposit.handler;

import com.nttdata.deposit.model.dto.DepositDto;
import com.nttdata.deposit.model.entity.Account;
import com.nttdata.deposit.model.entity.Association;
import com.nttdata.deposit.model.entity.Deposit;
import com.nttdata.deposit.model.entity.Movement;
import com.nttdata.deposit.service.IAccountService;
import com.nttdata.deposit.service.IAssociationService;
import com.nttdata.deposit.service.IDepositService;
import com.nttdata.deposit.service.IMovementService;
import com.nttdata.deposit.util.Commission;
import com.nttdata.deposit.util.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DepositHandler {

    private final IDepositService service;
    private final IAccountService iAccountService;
    private final IMovementService iMovementService;
    private final IAssociationService iAssociationService;

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<DepositDto> depositDto = request.bodyToMono(DepositDto.class);
        Account account = new Account();
        Deposit deposit = new Deposit();
        Association association = new Association();
        return depositDto
                .flatMap(dto -> iAccountService.findByAccountNumber(dto.getAccountNumber())
                                                        .map(a -> {
                                                            account.setBalance(a.getBalance());
                                                            account.setAccountNumber(a.getAccountNumber());
                                                            account.setAccountType(a.getAccountType());
                                                            account.setMaximumMovementLimit(a.isMaximumMovementLimit());
                                                            account.setMovementAmount(a.getMovementAmount());
                                                            account.setCustomer(a.getCustomer());
                                                            account.setStatus(a.getStatus());
                                                            return dto;
                                                        })
                )
                /*.flatMap(dto -> iAssociationService.findByAccountNumberAndStatus(dto.getAccountNumber(), "ASSOCIATED")
                                                        .map(as -> {
                                                            if (as != null){
                                                                association.setCardNumber(as.getCardNumber());
                                                                association.setCardType(as.getCardType());
                                                            }
                                                            return dto;
                                                        })
                                                        .onErrorResume(error -> {
                                                            WebClientResponseException errorResponse = (WebClientResponseException) error;
                                                            if (errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                                                                return Mono.just(dto);
                                                            }
                                                            return Mono.error(errorResponse);
                                                        })
                )*/
                .map(dto -> {
                    deposit.setAccount(account);
                    deposit.setOperationNumber(Generator.generateOperationNumber());
                    deposit.setAmount(dto.getAmount());
                    deposit.setStatus("CREATED");
                    return deposit;
                })
                .flatMap(d -> service.save(d)
                        .flatMap(dep -> {
                            double balanceNew = deposit.getAmount() + account.getBalance();
                            account.setBalance(balanceNew);
                            return iAccountService.updateAmountAccount(account)
                                    .flatMap(a -> {
                                        Movement movement = new Movement();
                                        movement.setOperationNumber(dep.getOperationNumber());
                                        movement.setAccountNumber(account.getAccountNumber());
                                        //movement.setCardNumber(association.getCardNumber());
                                        movement.setCardNumber("");
                                        movement.setMovementType("DEPOSIT");
                                        movement.setAccountType(account.getAccountType());
                                        //movement.setCardType(association.getCardType());
                                        movement.setCardType("");
                                        movement.setDocumentNumber(account.getCustomer().getDocumentNumber());
                                        movement.setAmount(dep.getAmount());
                                        movement.setConcept("DEPOSIT ACCOUNT");
                                        movement.setStatus("PROCESSED");
                                        return iMovementService.save(movement)
                                                .flatMap(m -> iMovementService.findByMovementDateBetweenAndAccountNumber(a.getAccountNumber())
                                                                                .filter(mo -> mo.getMovementType().equals("DEPOSIT") || mo.getMovementType().equals("RETIRE"))
                                                                                .count()
                                                                                .flatMap(c -> {
                                                                                    if (Math.toIntExact(c) > account.getMovementAmount()){
                                                                                        Account accountC = new Account();
                                                                                        double balanceCommission = a.getBalance() - Commission.getCommission(a.getAccountType());
                                                                                        accountC.setBalance(balanceCommission);
                                                                                        accountC.setAccountNumber(a.getAccountNumber());
                                                                                        return iAccountService.updateAmountAccount(accountC)
                                                                                                .flatMap(mo -> {
                                                                                                    Movement movementCommission = new Movement();
                                                                                                    movementCommission.setOperationNumber(dep.getOperationNumber());
                                                                                                    movementCommission.setAccountNumber(account.getAccountNumber());
                                                                                                    //movementCommission.setCardNumber(association.getCardNumber());
                                                                                                    movementCommission.setCardNumber("");
                                                                                                    movementCommission.setMovementType("COMMISSION");
                                                                                                    movementCommission.setAccountType(account.getAccountType());
                                                                                                    //movementCommission.setCardType(association.getCardType());
                                                                                                    movementCommission.setCardType("");
                                                                                                    movementCommission.setDocumentNumber(account.getCustomer().getDocumentNumber());
                                                                                                    movementCommission.setAmount(dep.getAmount());
                                                                                                    movementCommission.setConcept("COMMISSION TO DEPOSIT ACCOUNT");
                                                                                                    movementCommission.setStatus("PROCESSED");
                                                                                                    return iMovementService.save(movementCommission)
                                                                                                            .map(mc -> m);
                                                                                                });
                                                                                    }
                                                                                    return Mono.just(m);
                                                                                }));
                                    });
                        }))
                .flatMap(d -> ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(d)
                );
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        return service.findById(id)
                .flatMap(c -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(c)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
