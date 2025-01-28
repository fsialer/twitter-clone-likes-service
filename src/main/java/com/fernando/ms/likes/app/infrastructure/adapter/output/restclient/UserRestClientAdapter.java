package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements ExternalUserOutputPort {
    private final UserWebClient userWebClient;
    @Override
    public Mono<Boolean> verify(Long id) {
        return userWebClient.verify(id)
                .flatMap(existsPostResponse -> {
                    return Mono.just(existsPostResponse.getExists());
                });
    }
}
