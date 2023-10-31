package com.demo.exchangeRate.router;

import com.demo.exchangeRate.handler.ExchangeRateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateRouter {

    private static final String PATH = "exchangeRate";

    
    @Bean
    RouterFunction<ServerResponse> routes(ExchangeRateHandler handler){
        return RouterFunctions.route()
                .GET(PATH, handler::getAll)
                .GET(PATH + "/{id}", handler::getById)
                .POST(PATH, handler::save)
                .PUT(PATH + "/{id}", handler::update)
                .DELETE(PATH + "/{id}", handler::delete)
                .build();
    }
}
