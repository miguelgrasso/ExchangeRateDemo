package com.demo.exchangeRate.service;

import com.demo.exchangeRate.dto.ExchangeRateDto;
import com.demo.exchangeRate.entity.ExchangeRate;
import com.demo.exchangeRate.exception.ExchangeRateException;
import com.demo.exchangeRate.repository.ExchangeRateRepository;
import com.demo.exchangeRate.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final JwtProvider jwtProvider;
    private final static String NF_MESSAGE = "exchange rate not found";

    private final static String NFU_MESSAGE = "user not found";

    public Flux<ExchangeRate> getAll() {
        return exchangeRateRepository.findAll();
    }

    public Mono<ExchangeRate> getById(int id) {
        return exchangeRateRepository.findById(id).switchIfEmpty(Mono.error(new ExchangeRateException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    public Mono<ExchangeRate> save(ExchangeRateDto exchangeRateDto, String token) {
        String username = getUsuario(token);
        return exchangeRateRepository.save(ExchangeRate.builder()
                .amount(exchangeRateDto.getAmount())
                .sourceCurrency(exchangeRateDto.getSourceCurrency())
                .targetCurrency(exchangeRateDto.getTargetCurrency())
                .rate(exchangeRateDto.getRate())
                .resultAmount(exchangeRateDto.getAmount() * exchangeRateDto.getRate())
                .username(username)
                .dateTransaction(LocalDateTime.now())
                .build());
    }

    public Mono<ExchangeRate> update(int id, ExchangeRateDto exchangeRateDto, String token) {
        String username = getUsuario(token);
        Mono<Boolean> exchangeRateId = exchangeRateRepository.findById(id).hasElement();

        return exchangeRateId.flatMap(exists -> exists ?
                exchangeRateRepository.findById(id)
                        .flatMap(p -> {
                            p.setAmount(exchangeRateDto.getAmount());
                            p.setSourceCurrency(exchangeRateDto.getSourceCurrency());
                            p.setTargetCurrency(exchangeRateDto.getTargetCurrency());
                            p.setRate(exchangeRateDto.getRate());
                            p.setResultAmount(exchangeRateDto.getAmount() * exchangeRateDto.getRate());
                            p.setUsername(username);
                            p.setDateTransaction(LocalDateTime.now());
                            return exchangeRateRepository.save(p);
                        }) :
                Mono.error(new ExchangeRateException(HttpStatus.NOT_FOUND, NF_MESSAGE)));

    }

    public Mono<Void> delete(int id) {
        Mono<Boolean> exchangeRateId = exchangeRateRepository.findById(id).hasElement();
        return exchangeRateId.flatMap(exists -> exists ? exchangeRateRepository.deleteById(id) : Mono.error(new ExchangeRateException(HttpStatus.NOT_FOUND, NF_MESSAGE)));

    }



    public String getUsuario(String token) {

        return jwtProvider.getSubject(token.replace("Bearer ", ""));


    }
}
