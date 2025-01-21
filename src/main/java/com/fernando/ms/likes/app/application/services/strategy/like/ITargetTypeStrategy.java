package com.fernando.ms.likes.app.application.services.strategy.like;

import com.fernando.ms.likes.app.domain.models.Like;
import reactor.core.publisher.Mono;

public interface ITargetTypeStrategy {
    Mono<Like> doOperation(Like like);
    boolean isApplicable(String targetType);
}
