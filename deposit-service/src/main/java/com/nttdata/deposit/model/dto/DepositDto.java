package com.nttdata.deposit.model.dto;

import lombok.Data;

@Data
public class DepositDto {
    private double amount;
    private String accountNumber;
}
