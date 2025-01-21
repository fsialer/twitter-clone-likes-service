package com.fernando.ms.likes.app.application.ports.output;

import reactor.core.publisher.Mono;

public interface ExternalPostOutputPort {
    Mono<Boolean> verify(String id);
}
