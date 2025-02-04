package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper.LikeRestMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.LikeResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.QuantityLikeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/likes")
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
    public Mono<ResponseEntity<LikeResponse>> save(@RequestHeader("X-User-Id") Long userId,@Valid @RequestBody CreateLikeRequest rq){
        return likeInputPort.save(likeRestMapper.toLike(userId,rq))
                .flatMap(like->{
                    String location = "/likes/".concat(like.getId());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(likeRestMapper.toLikeResponse(like)));
                });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/unlike/{targetType}/target-type/{targetId}/target-id")
    public Mono<Void> unlike(@RequestHeader("X-User-Id") Long userId, @PathVariable("targetType") String targetType,@PathVariable("targetId") String targetId){
        return likeInputPort.unlike(userId,targetType,targetId);
    }
}
