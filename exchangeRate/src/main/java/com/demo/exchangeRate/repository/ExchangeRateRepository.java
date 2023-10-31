package com.demo.exchangeRate.repository;

import com.demo.exchangeRate.entity.ExchangeRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, Integer> {
}
