package com.fernando.ms.likes.app.application.services;

import com.fernando.ms.likes.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.likes.app.application.ports.output.LikePersistencePort;
import com.fernando.ms.likes.app.application.services.strategy.like.ITargetTypeStrategy;
import com.fernando.ms.likes.app.domain.exception.LikeNotFoundException;
import com.fernando.ms.likes.app.domain.exception.TargetTypeNotFoundException;
import com.fernando.ms.likes.app.domain.exception.UniqueLikeException;
import com.fernando.ms.likes.app.domain.exception.UserNotFoundException;
import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.domain.models.User;
import com.fernando.ms.likes.app.utils.TestUtilsLike;
import com.fernando.ms.likes.app.utils.TestUtilsUser;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @Mock
    private LikePersistencePort likePersistencePort;

    @Mock
    private ExternalUserOutputPort externalUserOutputPort;

    @Mock
    private List<ITargetTypeStrategy> targetTypeStrategyList;

    @Mock
    private ITargetTypeStrategy targetTypeStrategy;

    @InjectMocks
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        targetTypeStrategyList = List.of(targetTypeStrategy);
        likeService=new LikeService(likePersistencePort,externalUserOutputPort,targetTypeStrategyList);
    }

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

    @Test
    @DisplayName("When Like Information Is Correct Expect Like Saved Successfully")
    void When_LikeInformationIsCorrect_Expect_LikeSavedSuccessfully2() {
        Like like=TestUtilsLike.buildLikeMock();

        when(targetTypeStrategy.isApplicable(anyString())).thenReturn(true);
        when(targetTypeStrategy.doOperation(any(Like.class))).thenReturn(Mono.just(like));
        when(likePersistencePort.existsByUserAndTargetTypeTargetId(any(), anyString(), anyString())).thenReturn(Mono.just(false));
        when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(true));
        when(likePersistencePort.save(any(Like.class))).thenReturn(Mono.just(like));

        Mono<Like> result = likeService.save(like);

        StepVerifier.create(result)
                .expectNext(like)
                .verifyComplete();

        Mockito.verify(targetTypeStrategy, times(1)).isApplicable(anyString());
        Mockito.verify(targetTypeStrategy, times(1)).doOperation(any(Like.class));
        Mockito.verify(likePersistencePort, times(1)).existsByUserAndTargetTypeTargetId(any(), anyString(), anyString());
        Mockito.verify(externalUserOutputPort, times(1)).verify(anyLong());
        Mockito.verify(likePersistencePort, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("When Like Already Exists Expect UniqueLikeException")
    void When_LikeAlreadyExists_Expect_UniqueLikeException() {
        Like like=TestUtilsLike.buildLikeMock();
        when(likePersistencePort.existsByUserAndTargetTypeTargetId(any(), anyString(), anyString())).thenReturn(Mono.just(true));
        Mono<Like> result = likeService.save(like);

        StepVerifier.create(result)
                .expectError(UniqueLikeException.class)
                .verify();
        Mockito.verify(targetTypeStrategy, never()).isApplicable(anyString());
        Mockito.verify(targetTypeStrategy, never()).doOperation(any(Like.class));
        Mockito.verify(likePersistencePort, times(1)).existsByUserAndTargetTypeTargetId(any(), anyString(), anyString());
        Mockito.verify(externalUserOutputPort, never()).verify(anyLong());
        Mockito.verify(likePersistencePort,never()).save(any(Like.class));
    }


    @Test
    @DisplayName("When User Not Found Expect UserNotFoundException")
    void wWen_UserNotFound_Expect_UserNotFoundException() {
        Like like=TestUtilsLike.buildLikeMock();
        when(likePersistencePort.existsByUserAndTargetTypeTargetId(any(), anyString(), anyString())).thenReturn(Mono.just(false));
        when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(false));

        Mono<Like> result = likeService.save(like);

        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();
        Mockito.verify(targetTypeStrategy, never()).isApplicable(anyString());
        Mockito.verify(targetTypeStrategy, never()).doOperation(any(Like.class));
        Mockito.verify(likePersistencePort, times(1)).existsByUserAndTargetTypeTargetId(any(), anyString(), anyString());
        Mockito.verify(externalUserOutputPort, times(1)).verify(anyLong());
        Mockito.verify(likePersistencePort,never()).save(any(Like.class));
    }


    @Test
    @DisplayName("When Target Type Not Found Expect TargetTypeNotFoundException")
    void When_TargetTypeNotFound_Expect_TargetTypeNotFoundException() {
        Like like=TestUtilsLike.buildLikeMock();
        when(likePersistencePort.existsByUserAndTargetTypeTargetId(any(), anyString(), anyString())).thenReturn(Mono.just(false));
        when(externalUserOutputPort.verify(anyLong())).thenReturn(Mono.just(true));
        when(targetTypeStrategy.isApplicable(anyString())).thenReturn(false);

        Mono<Like> result = likeService.save(like);

        StepVerifier.create(result)
                .expectError(TargetTypeNotFoundException.class)
                .verify();
        Mockito.verify(targetTypeStrategy,  times(1)).isApplicable(anyString());
        Mockito.verify(targetTypeStrategy, never()).doOperation(any(Like.class));
        Mockito.verify(likePersistencePort, times(1)).existsByUserAndTargetTypeTargetId(any(), anyString(), anyString());
        Mockito.verify(externalUserOutputPort, times(1)).verify(anyLong());
        Mockito.verify(likePersistencePort,never()).save(any(Like.class));
    }


    @Test
    @DisplayName("When Unlike Is Successful Expect Void")
    void when_UnlikeIsSuccessful_Expect_Void() {
        Like like=TestUtilsLike.buildLikeMock();
        when(likePersistencePort.findByLikeUserAndTargetTypeAndTargetId(any(User.class), anyString(), anyString())).thenReturn(Mono.just(like));
        when(likePersistencePort.delete(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = likeService.unlike(1L, "POST", "67831b0ec8dda45d9a6c3022");

        StepVerifier.create(result)
                .verifyComplete();
        Mockito.verify(likePersistencePort, times(1)).findByLikeUserAndTargetTypeAndTargetId(any(User.class), anyString(), anyString());
        Mockito.verify(likePersistencePort,times(1)).delete(anyString());
    }

    @Test
    @DisplayName("Expect LikeNotFoundException When Like Not Found ")
    void Expect_LikeNotFoundException_When_LikeNotFound() {
        when(likePersistencePort.findByLikeUserAndTargetTypeAndTargetId(any(User.class), anyString(), anyString())).thenReturn(Mono.empty());

        Mono<Void> result = likeService.unlike(1L, "POST", "67831b0ec8dda45d9a6c3022");

        StepVerifier.create(result)
                .expectError(LikeNotFoundException.class)
                .verify();
        Mockito.verify(likePersistencePort, times(1)).findByLikeUserAndTargetTypeAndTargetId(any(User.class), anyString(), anyString());
        Mockito.verify(likePersistencePort,never()).delete(anyString());
    }
}
