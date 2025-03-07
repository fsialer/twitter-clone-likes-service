package com.fernando.ms.likes.app.application.services.proxy;

import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.application.services.strategy.like.ITargetTypeStrategy;
import com.fernando.ms.likes.app.domain.exception.TargetTypeNotFoundException;
import com.fernando.ms.likes.app.domain.exception.UniqueLikeException;
import com.fernando.ms.likes.app.domain.exception.UserNotFoundException;
import com.fernando.ms.likes.app.domain.models.Like;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class RuleSaveLikeProxy implements IProcess{

    private final LikePersistencePort likePersistencePort;
    private final ExternalUserOutputPort externalUserOutputPort;
    private final List<ITargetTypeStrategy> targetTypeStrategyList;
    @Override
    public Mono<Like> doProcess(Like like) {
        return likePersistencePort.existsByUserAndTargetTypeTargetId(like.getUser(),like.getTargetType(),like.getTargetId())
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(new UniqueLikeException("Like is unique by user.")))
                .flatMap(uniqueLike->
                     externalUserOutputPort.verify(like.getUser().getId())
                            .filter(Boolean.TRUE::equals)
                            .switchIfEmpty(Mono.error(UserNotFoundException::new))
                            .flatMap(existsUser->{
                                ITargetTypeStrategy targetTypeStrategy= targetTypeStrategyList.stream()
                                        .filter(strategy->strategy.isApplicable(like.getTargetType()))
                                        .findFirst()
                                        .orElseThrow(()->new TargetTypeNotFoundException("Target type ".concat(like.getTargetType()).concat(" no exists.")));
                                return targetTypeStrategy.doOperation(like);
                            })
                );
    }
}
