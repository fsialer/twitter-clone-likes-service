package com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper;

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
}
