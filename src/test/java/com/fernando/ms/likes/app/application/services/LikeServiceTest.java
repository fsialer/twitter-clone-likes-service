package com.fernando.ms.likes.app.application.services;

import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.utils.TestUtilsLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @Mock
    private LikePersistencePort likePersistencePort;

    @InjectMocks
    private LikeService likeService;

    @Test
    @DisplayName("When TargetID And TargetType Are Correct Expect Quantity Like Exists")
    void When_TargetIDAndTargetTypeAreCorrect_Expect_QuantityLikeExists() {
        Like like=TestUtilsLike.buildLikeMock();

        when(likePersistencePort.findAllByTargetId(anyString())).thenReturn(Flux.just(like));

        Mono<Long> result = likeService.quantityLike("67831b0ec8dda45d9a6c3022", "POST");

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
        Mockito.verify(likePersistencePort, times(1)).findAllByTargetId(anyString());
    }
}
