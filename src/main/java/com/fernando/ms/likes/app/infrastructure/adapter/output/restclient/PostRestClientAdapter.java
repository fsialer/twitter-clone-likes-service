package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.application.ports.output.ExternalPostOutputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.PostWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostRestClientAdapter implements ExternalPostOutputPort {
    private final PostWebClient postWebClient;
    @Override
    public Mono<Boolean> verify(String id) {
        return postWebClient.verify(id)
                .flatMap(existsPostResponse -> {
                    return Mono.just(existsPostResponse.getExists());
                });
    }
}
