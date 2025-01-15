package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper.LikeRestMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.QuantityLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeRestAdapter {
    private final LikeInputPort likeInputPort;
    private final LikeRestMapper likeRestMapper;

    @GetMapping("/quantity/{targetId}/target/{targetType}/type")
    public Mono<ResponseEntity<QuantityLikeResponse>> quantityLike(@PathVariable("targetId") String targetId, @PathVariable("targetType") String targetType){
        return  likeInputPort.quantityLike(targetId,targetType)
                .flatMap(like->{
                    return Mono.just(ResponseEntity.ok().body(likeRestMapper.toQuantityLikeResponse(like)));
                });
    }
}
