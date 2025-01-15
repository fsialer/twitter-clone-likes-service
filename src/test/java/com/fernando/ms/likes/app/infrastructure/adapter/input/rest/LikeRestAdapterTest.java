package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper.LikeRestMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.LikeResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.QuantityLikeResponse;
import com.fernando.ms.likes.app.utils.TestUtilsLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {LikeRestAdapter.class})
public class LikeRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LikeInputPort likeInputPort;

    @MockBean
    private LikeRestMapper likeRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("When TargetId And TargetType Are Exists Expect Quantity Likes Successfully")
    void When_TargetIdAndTargetTypeAreExists_Expect_QuantityLikesSuccessfully() {
        QuantityLikeResponse quantityLikeResponse= TestUtilsLike.buildQuantityLikeResponseMock();
        when(likeInputPort.quantityLike(anyString(),anyString())).thenReturn(Mono.just(1L));
        when(likeRestMapper.toQuantityLikeResponse(anyLong())).thenReturn(quantityLikeResponse);

        webTestClient.get()
                .uri("/likes/quantity/{targetId}}/target/{targetType}/type","67831b0ec8dda45d9a6c3022","POST")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.quantity").isEqualTo(1L);

        Mockito.verify(likeInputPort, times(1)).quantityLike(anyString(), anyString());
        Mockito.verify(likeRestMapper, times(1)).toQuantityLikeResponse(anyLong());



    }

    @Test
    @DisplayName("When Save Like Expect Like Saved Successfully")
    void when_SaveLike_Expect_LikeSavedSuccessfully() {
        CreateLikeRequest createLikeRequest = TestUtilsLike.buildCreateLikeRequestMock();
        LikeResponse likeResponse = TestUtilsLike.buildLikeResponseMock();

        when(likeInputPort.save(any())).thenReturn(Mono.just(TestUtilsLike.buildLikeMock()));
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeRestMapper.toLikeResponse(any())).thenReturn(likeResponse);

        webTestClient.post()
                .uri("/likes")
                .bodyValue(createLikeRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(likeResponse.getId());

        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(1)).toLikeResponse(any());
    }

}
