package com.fernando.ms.likes.app.application.ports.output;

import reactor.core.publisher.Mono;

public interface ExternalUserOutputPort {
    Mono<Boolean> verify(Long id);
}
