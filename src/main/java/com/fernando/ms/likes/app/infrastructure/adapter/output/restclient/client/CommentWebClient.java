package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsCommentResponse;
import reactor.core.publisher.Mono;

public interface CommentWebClient {
    Mono<ExistsCommentResponse> verify(String id);
}
