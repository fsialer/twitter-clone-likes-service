package com.fernando.ms.likes.app.application.services.strategy.like;

import com.fernando.ms.likes.app.application.ports.output.ExternalCommentOutputPort;
import com.fernando.ms.likes.app.domain.exception.CommentNotFoundException;
import com.fernando.ms.likes.app.domain.models.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentTargetTypeStrategy implements ITargetTypeStrategy{
    private final ExternalCommentOutputPort externalCommentOutputPort;
    @Override
    public Mono<Like> doOperation(Like like) {
        return externalCommentOutputPort.verify(like.getTargetId())
                .flatMap(verify->{
                    if(Boolean.FALSE.equals(verify)){
                        return Mono.error(CommentNotFoundException::new);
                    }
                    return Mono.just(like);
                });
    }

    @Override
    public boolean isApplicable(String targetType) {
        return "COMMENT".equals(targetType);
    }
}
