package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.application.ports.output.ExternalCommentOutputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.CommentWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentRestClientAdapter implements ExternalCommentOutputPort {

    private final CommentWebClient commentWebClient;

    @Override
    public Mono<Boolean> verify(String id) {
        return commentWebClient.verify(id)
                .flatMap(existsPostResponse -> {
                    return Mono.just(existsPostResponse.getExists());
                });
    }
}
