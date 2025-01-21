package com.fernando.ms.likes.app.application.services;

import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.application.services.strategy.like.ITargetTypeStrategy;
import com.fernando.ms.likes.app.domain.exception.TargetTypeNotFoundException;
import com.fernando.ms.likes.app.domain.exception.UniqueLikeException;
import com.fernando.ms.likes.app.domain.exception.UserNotFoundException;
import com.fernando.ms.likes.app.domain.models.Like;
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
        return likePersistencePort.existsByUserAndTargetTypeTargetId(like.getUser(),like.getTargetType(),like.getTargetId())
                        .flatMap(uniqueLike->{
                            if(Boolean.TRUE.equals(uniqueLike)){
                                return Mono.error(new UniqueLikeException("Like is unique by user."));
                            }
                           return externalUserOutputPort.verify(like.getUser().getId())
                                    .flatMap(existsUser->{
                                        if(Boolean.FALSE.equals(existsUser)){
                                            return Mono.error(UserNotFoundException::new);
                                        }
                                        ITargetTypeStrategy targetTypeStrategy= targetTypeStrategyList.stream()
                                                .filter(strategy->strategy.isApplicable(like.getTargetType()))
                                                .findFirst()
                                                .orElseThrow(()->new TargetTypeNotFoundException("Target type ".concat(like.getTargetType()).concat(" no exists.")));
                                        return targetTypeStrategy.doOperation(like)
                                                .flatMap(likeSave->{
                                                    return likePersistencePort.save(like);
                                                });
                                    });
                        });
    }
}
