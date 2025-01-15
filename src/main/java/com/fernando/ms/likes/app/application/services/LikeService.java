package com.fernando.ms.likes.app.application.services;

import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LikeService implements LikeInputPort {

    private final LikePersistencePort likePersistencePort;
    @Override
    public Mono<Long> quantityLike(String targetId, String targetType) {
        return likePersistencePort.findAllByTargetId(targetId)
                .filter(like -> like.getTargetType().equals(targetType))
                .count();
    }
}
