package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.application.ports.output.ExternalCommentOutputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsCommentResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentRestClientAdapter implements ExternalCommentOutputPort {
    private final WebClient webClientComment;

    @Override
    public Mono<Boolean> verify(String id) {
        return  webClientComment
                .get()
                .uri("/comments/{id}/verify",id)
                .retrieve()
                .bodyToMono(ExistsCommentResponse.class)
                .flatMap(existsPostResponse -> {
                    return Mono.just(existsPostResponse.getExists());
                });
    }
}
