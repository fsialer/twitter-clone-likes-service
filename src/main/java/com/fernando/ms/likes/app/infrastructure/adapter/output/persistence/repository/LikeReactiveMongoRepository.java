package com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikeReactiveMongoRepository extends ReactiveMongoRepository<LikeDocument,String> {
    Flux<LikeDocument> findAllByTargetId(String targetId);
}
