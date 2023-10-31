package com.demo.exchangeRate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "exchangeRate")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExchangeRate {
    @Id
    private int id;
    private double amount;
    private String sourceCurrency;
    private String targetCurrency;
    private double rate;
    private double resultAmount;
    private String username;
    private LocalDateTime dateTransaction;

}
