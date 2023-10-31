package com.demo.exchangeRate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExchangeRateDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0.0")
    private double amount;
    @NotBlank(message = "sourceCurrency is mandatory")
    private String sourceCurrency;
    @NotBlank(message = "targetCurrency is mandatory")
    private String targetCurrency;
    @DecimalMin(value = "0.0", inclusive = false, message = "Rate must be greater than 0.0")
    private double rate;
}
