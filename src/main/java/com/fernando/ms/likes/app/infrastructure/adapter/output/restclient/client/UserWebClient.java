package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsPostResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import reactor.core.publisher.Mono;

public interface UserWebClient {
    Mono<ExistsUserResponse> verify(Long id);
}
