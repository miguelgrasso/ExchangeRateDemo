package com.demo.exchangeRate.security.service;

import com.demo.exchangeRate.exception.ExchangeRateException;
import com.demo.exchangeRate.security.dto.CreateUserDto;
import com.demo.exchangeRate.security.dto.LoginDto;
import com.demo.exchangeRate.security.dto.TokenDto;
import com.demo.exchangeRate.security.entity.User;
import com.demo.exchangeRate.security.enums.Role;
import com.demo.exchangeRate.security.jwt.JwtProvider;
import com.demo.exchangeRate.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDto> login(LoginDto dto) {
        return userRepository.findByUsername(dto.getUsername())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> new TokenDto(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new ExchangeRateException(HttpStatus.BAD_REQUEST, "bad credentials")));
    }

    public Mono<User> create(CreateUserDto dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Role.ROLE_USER.name())
                .build();
        Mono<Boolean> userExists = userRepository.findByUsername(user.getUsername()).hasElement();
        return userExists
                .flatMap(exists -> exists ?
                        Mono.error(new ExchangeRateException(HttpStatus.BAD_REQUEST, "username already in use"))
                        : userRepository.save(user));
    }

    public Flux<User> getAll() {
        return userRepository.findAll();
    }
}