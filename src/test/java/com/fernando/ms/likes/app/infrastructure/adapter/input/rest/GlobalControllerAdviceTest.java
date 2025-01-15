package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.likes.app.application.ports.input.LikeInputPort;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.mapper.LikeRestMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.enums.ErrorType;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.LikeResponse;
import com.fernando.ms.likes.app.infrastructure.utils.ErrorCatalog;
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

import static com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.likes.app.infrastructure.utils.ErrorCatalog.INTERNAL_SERVER_ERROR;
import static com.fernando.ms.likes.app.infrastructure.utils.ErrorCatalog.LIKE_BAD_PARAMETERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @DisplayName("When Exception Occurs Expect Internal Server Error Response")
    void handleExceptionTest() throws JsonProcessingException {
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


}
