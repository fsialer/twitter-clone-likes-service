package com.fernando.ms.likes.app.application.services.strategy.like;

import com.fernando.ms.likes.app.application.ports.output.ExternalPostOutputPort;
import com.fernando.ms.likes.app.domain.exception.CommentNotFoundException;
import com.fernando.ms.likes.app.domain.exception.PostNotFoundException;
import com.fernando.ms.likes.app.domain.models.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostTargetTypeStrategy implements ITargetTypeStrategy{
    private final ExternalPostOutputPort externalPostOutputPort;
    @Override
    public Mono<Like> doOperation(Like like) {
        return externalPostOutputPort.verify(like.getTargetId())
                .flatMap(verify->{
                    if(Boolean.FALSE.equals(verify)){
                        return Mono.error(PostNotFoundException::new);
                    }
                    return Mono.just(like);
                });
    }

    @Override
    public boolean isApplicable(String targetType) {
        return "POST".equals(targetType);
    }
}
