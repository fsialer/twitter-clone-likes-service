package com.fernando.ms.likes.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.mapper.LikePersistenceMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeDocument;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeUser;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.repository.LikeReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LikePersistenceAdapter implements LikePersistencePort {
    private final LikeReactiveMongoRepository likeReactiveMongoRepository;
    private final LikePersistenceMapper likePersistenceMapper;

    @Override
    public Flux<Like> findAllByTargetId(String targetId) {
        return likePersistenceMapper.toLikes(likeReactiveMongoRepository.findAllByTargetId(targetId));
    }

    @Override
    public Mono<Like> save(Like like) {
        LikeDocument likeDocument=likePersistenceMapper.toLikeDocument(like);
        LikeUser likeUser=LikeUser.builder().userId(like.getUser().getId()).build();
        likeDocument.setLikeUser(likeUser);
        return likePersistenceMapper.toLike(likeReactiveMongoRepository.save(likeDocument));
    }
}
