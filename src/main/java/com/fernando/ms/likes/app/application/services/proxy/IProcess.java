package com.fernando.ms.likes.app.application.services.proxy;

import com.fernando.ms.likes.app.domain.models.Like;
import reactor.core.publisher.Mono;

public interface IProcess {
    Mono<Like> doProcess(Like like);
}
