package com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.mapper;

import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeDocument;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;

@Mapper(componentModel = "spring")
public interface LikePersistenceMapper {
    default Flux<Like> toLikes(Flux<LikeDocument> likes){
        return likes.map(this::toLike);
    }

    Like toLike(LikeDocument like);
}
