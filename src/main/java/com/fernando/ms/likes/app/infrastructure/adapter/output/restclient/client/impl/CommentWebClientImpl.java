package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.impl;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.CommentWebClient;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentWebClientImpl implements CommentWebClient {
    private final WebClient webClientComment;
    @Override
    public Mono<ExistsCommentResponse> verify(String id) {
        return webClientComment
                .get()
                .uri("/{id}/verify",id)
                .retrieve()
                .bodyToMono(ExistsCommentResponse.class);
    }
}
