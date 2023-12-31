package com.carrental.cloudgateway.handler;

import com.carrental.document.dto.AuthenticationRequest;
import com.carrental.lib.security.jwt.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationHandler {

    private final JwtAuthenticationService jwtAuthenticationService;

    public Mono<ServerResponse> authenticateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthenticationRequest.class)
                .flatMap(jwtAuthenticationService::authenticateUser)
                .flatMap(authenticationResponse -> ServerResponse.ok().bodyValue(authenticationResponse))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

}
