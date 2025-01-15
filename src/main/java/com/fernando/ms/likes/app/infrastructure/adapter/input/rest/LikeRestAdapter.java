package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper.LikeRestMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.LikeResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.QuantityLikeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

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

    @PostMapping
    public Mono<ResponseEntity<LikeResponse>> save(@Valid @RequestBody CreateLikeRequest rq){
        return likeInputPort.save(likeRestMapper.toLike(rq))
                .flatMap(like->{
                    String location = "/likes/".concat(like.getId());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(likeRestMapper.toLikeResponse(like)));
                });
    }
}
