package com.fernando.ms.likes.app.application.ports.input;

import com.fernando.ms.likes.app.domain.models.Like;
import reactor.core.publisher.Mono;

public interface LikeInputPort {
    Mono<Long> quantityLike(String targetId,String targetType);
    Mono<Like> save(Like like);
}
