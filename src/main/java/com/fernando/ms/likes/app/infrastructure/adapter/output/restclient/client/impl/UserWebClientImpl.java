package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.impl;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserWebClientImpl implements UserWebClient {
    private final WebClient webClientUser;
    @Override
    public Mono<ExistsUserResponse> verify(Long id) {
        return webClientUser
                .get()
                .uri("/users/{id}/verify",id)
                .retrieve()
                .bodyToMono(ExistsUserResponse.class);

    }
}
