package com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper;

import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.domain.models.User;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.LikeResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.QuantityLikeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface LikeRestMapper {
    @Mapping(target = "quantity",expression = "java(mapQuantity(quantity))")
    QuantityLikeResponse toQuantityLikeResponse(Long quantity);

    default Long mapQuantity(Long quantity){
        return  quantity;
    }

    @Mapping(target = "user", expression = "java(mapUser(userId))")
    Like toLike(Long userId,CreateLikeRequest rq);

    default User mapUser(Long userId){
        return User.builder().id(userId).build();
    }

    LikeResponse toLikeResponse(Like rq);
}
