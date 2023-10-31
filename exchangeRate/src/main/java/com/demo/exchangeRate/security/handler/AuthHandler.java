package com.demo.exchangeRate.security.handler;

import com.demo.exchangeRate.security.dto.CreateUserDto;
import com.demo.exchangeRate.security.dto.LoginDto;
import com.demo.exchangeRate.security.dto.TokenDto;
import com.demo.exchangeRate.security.entity.User;
import com.demo.exchangeRate.security.service.UserService;
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
public class AuthHandler {

    private final UserService userService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.login(dto), TokenDto.class));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.create(dto), User.class));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAll(), User.class);
    }
}
