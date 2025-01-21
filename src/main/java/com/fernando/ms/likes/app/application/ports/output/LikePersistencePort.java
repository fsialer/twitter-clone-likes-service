package com.fernando.ms.likes.app.application.ports.output;

import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikePersistencePort {
    Flux<Like> findAllByTargetId(String targetId);
    Mono<Like> save(Like like);
    Mono<Boolean> existsByUserAndTargetTypeTargetId(User user, String targetType, String targetId);
}
