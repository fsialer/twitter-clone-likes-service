package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.domain.exception.*;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper.LikeRestMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.likes.app.utils.TestUtilsLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.likes.app.infrastructure.utils.ErrorCatalog.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {LikeRestAdapter.class})
public class GlobalControllerAdviceTest {

    @MockBean
    private LikeRestMapper likeRestMapper;

    @MockBean
    private LikeInputPort likeInputPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Expect RuntimeException When Occurs Exception")
    void Expect_RuntimeException_When_OccursException() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= CreateLikeRequest.builder()
                .userId(1L)
                .targetType("POST")
                .targetId("67831b0ec8dda45d9a6c3022")
                .build();
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeInputPort.save(any())).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(INTERNAL_SERVER_ERROR.getCode());
                    assert response.getType().equals(SYSTEM);
                    assert response.getMessage().equals(INTERNAL_SERVER_ERROR.getMessage());
                    assert response.getDetails().equals(Collections.singletonList("Unexpected error"));
                });
        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(0)).toLikeResponse(any());
    }

    @Test
    @DisplayName("Expect WebExchangeBindException When Like Information Is Invalid")
    void Expect_WebExchangeBindException_When_LikeInformationIsInvalid() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= CreateLikeRequest.builder()
                .userId(1L)
                .targetType("POST")
                .targetId("")
                .build();

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(LIKE_BAD_PARAMETERS.getCode());
                    assert response.getMessage().equals(LIKE_BAD_PARAMETERS.getMessage());
                });
    }

    @Test
    @DisplayName("Expect UniqueLikeException When User TargetType TargetId Are Equals")
    void Expect_UniqueLikeException_When_UserTargetTypeTargetIdAreEquals() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= TestUtilsLike.buildCreateLikeRequestMock();
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeInputPort.save(any())).thenReturn(Mono.error(new UniqueLikeException("Like is unique by user")));

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(UNIQUE_LIKE_RULE.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(UNIQUE_LIKE_RULE.getMessage());
                    assert response.getDetails().equals(Collections.singletonList("Like is unique by user"));
                });
        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(0)).toLikeResponse(any());
    }

    @Test
    @DisplayName("Expect UserNotFoundException When User Not Exists")
    void Expect_UserNotFoundException_When_UserUserNotExists() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= TestUtilsLike.buildCreateLikeRequestMock();
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeInputPort.save(any())).thenReturn(Mono.error(new UserNotFoundException()));

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(USER_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(USER_NOT_FOUND.getMessage());
                });
        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(0)).toLikeResponse(any());
    }

    @Test
    @DisplayName("Expect PostNotFoundException When Post Not Exists")
    void Expect_PostNotFoundException_When_PostNotExists() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= TestUtilsLike.buildCreateLikeRequestMock();
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeInputPort.save(any())).thenReturn(Mono.error(new PostNotFoundException()));

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(POST_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(POST_NOT_FOUND.getMessage());
                });
        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(0)).toLikeResponse(any());
    }

    @Test
    @DisplayName("Expect CommentNotFoundException When Comment Not Exists")
    void Expect_CommentNotFoundException_When_CommentNotExists() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= TestUtilsLike.buildCreateLikeRequestMock();
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeInputPort.save(any())).thenReturn(Mono.error(new CommentNotFoundException()));

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(COMMENT_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(COMMENT_NOT_FOUND.getMessage());
                });
        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(0)).toLikeResponse(any());
    }

    @Test
    @DisplayName("Expect TargetTypeNotFoundException When Target Type Not Exists")
    void Expect_TargetTypeNotFoundException_When_TargetTypeNoExists() throws JsonProcessingException {
        CreateLikeRequest createLikeRequest= TestUtilsLike.buildCreateLikeRequestMock();
        when(likeRestMapper.toLike(any(CreateLikeRequest.class))).thenReturn(TestUtilsLike.buildLikeMock());
        when(likeInputPort.save(any())).thenReturn(Mono.error(new TargetTypeNotFoundException("Target type ".concat(createLikeRequest.getTargetType()).concat(" no exists."))));

        webTestClient.post()
                .uri("/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(createLikeRequest))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(TARGET_TYPE_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(TARGET_TYPE_NOT_FOUND.getMessage());
                    assert response.getDetails().equals(Collections.singletonList("Target type ".concat(createLikeRequest.getTargetType()).concat(" no exists.")));
                });
        Mockito.verify(likeInputPort, times(1)).save(any());
        Mockito.verify(likeRestMapper, times(1)).toLike(any(CreateLikeRequest.class));
        Mockito.verify(likeRestMapper, times(0)).toLikeResponse(any());
    }

    @Test
    @DisplayName("Expect LikeNotFoundException When Like Not Exists")
    void Expect_LikeNotFoundException_When_LikeNotExists() {

        when(likeInputPort.unlike(anyLong(),anyString(),anyString())).thenReturn(Mono.error(new LikeNotFoundException()));

        webTestClient.delete()
                .uri("/likes/unlike/{userId}/user/{targetType}/target-type/{targetId}/target-id", 1L, "POST", "67831b0ec8dda45d9a6c3022")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assert response.getCode().equals(LIKE_NOT_FOUND.getCode());
                    assert response.getType().equals(FUNCTIONAL);
                    assert response.getMessage().equals(LIKE_NOT_FOUND.getMessage());
                });
        Mockito.verify(likeInputPort, times(1)).unlike(anyLong(),anyString(),anyString());
    }

}
