package com.fernando.ms.likes.app.application.ports.input;

import reactor.core.publisher.Mono;

public interface LikeInputPort {
    Mono<Long> quantityLike(String targetId,String targetType);
}
