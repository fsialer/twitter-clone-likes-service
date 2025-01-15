package com.fernando.ms.likes.app.application.ports.output;

import com.fernando.ms.likes.app.domain.models.Like;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikePersistencePort {

    Flux<Like> findAllByTargetId(String targetId);
}
