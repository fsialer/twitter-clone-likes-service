package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsPostResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements ExternalUserOutputPort {
    private final WebClient webClientUser;
    @Override
    public Mono<Boolean> verify(Long id) {
        return  webClientUser
                .get()
                .uri("/users/{id}/verify",id)
                .retrieve()
                .bodyToMono(ExistsUserResponse.class)
                .flatMap(existsPostResponse -> {
                    return Mono.just(existsPostResponse.getExists());
                });
    }
}
