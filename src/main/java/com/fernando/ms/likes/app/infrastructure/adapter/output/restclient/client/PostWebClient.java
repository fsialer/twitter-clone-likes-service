package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsPostResponse;
import reactor.core.publisher.Mono;

public interface PostWebClient {
    Mono<ExistsPostResponse> verify(String id);
}
