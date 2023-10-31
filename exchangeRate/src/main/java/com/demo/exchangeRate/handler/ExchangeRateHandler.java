package com.demo.exchangeRate.handler;

import com.demo.exchangeRate.dto.ExchangeRateDto;
import com.demo.exchangeRate.entity.ExchangeRate;
import com.demo.exchangeRate.service.ExchangeRateService;
import com.demo.exchangeRate.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateHandler {

    private final ExchangeRateService exchangeRateService;
    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.getAll(), ExchangeRate.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.getById(id), ExchangeRate.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ExchangeRateDto> exchangeDtoRateMono = request.bodyToMono(ExchangeRateDto.class).doOnNext(objectValidator::validate);

        String token = request.exchange().getRequest().getHeaders().getFirst("Authorization");
        return exchangeDtoRateMono.flatMap(exchangeRate ->
                ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.save(exchangeRate,token), ExchangeRate.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        String token = request.exchange().getRequest().getHeaders().getFirst("Authorization");
        Mono<ExchangeRateDto> exchangeRateDtoMono = request.bodyToMono(ExchangeRateDto.class).doOnNext(objectValidator::validate);

        return exchangeRateDtoMono.flatMap(exchangeRateDto -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.update(id, exchangeRateDto,token), ExchangeRate.class));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.delete(id), Void.class);
    }
}
