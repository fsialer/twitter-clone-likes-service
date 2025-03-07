package com.fernando.ms.likes.app.application.services;

import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.application.services.proxy.IProcess;
import com.fernando.ms.likes.app.application.services.proxy.ProcessFactory;
import com.fernando.ms.likes.app.application.services.strategy.like.ITargetTypeStrategy;
import com.fernando.ms.likes.app.domain.exception.LikeNotFoundException;
import com.fernando.ms.likes.app.domain.exception.TargetTypeNotFoundException;
import com.fernando.ms.likes.app.domain.exception.UniqueLikeException;
import com.fernando.ms.likes.app.domain.exception.UserNotFoundException;
import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService implements LikeInputPort {

    private final LikePersistencePort likePersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;
    private final List<ITargetTypeStrategy> targetTypeStrategyList;


    @Override
    public Mono<Long> quantityLike(String targetId, String targetType) {
        return likePersistencePort.findAllByTargetId(targetId)
                .filter(like -> like.getTargetType().equals(targetType))
                .count();
    }

    @Override
    public Mono<Like> save(Like like) {
        IProcess process= ProcessFactory.validateSaveLike(likePersistencePort,externalUserOutputPort,targetTypeStrategyList);
        return process.doProcess(like).flatMap(likePersistencePort::save);

    }

    @Override
    public Mono<Void> unlike(Long userId, String targetType, String targetId) {
        User user= User.builder().id(userId).build();
        return likePersistencePort.findByLikeUserAndTargetTypeAndTargetId(user,targetType,targetId)
                .switchIfEmpty(Mono.error(LikeNotFoundException::new))
                .flatMap(like-> likePersistencePort.delete(like.getId()));
    }
}
