package com.fernando.ms.likes.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.domain.models.User;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.mapper.LikePersistenceMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeDocument;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeUser;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.repository.LikeReactiveMongoRepository;
import com.fernando.ms.likes.app.utils.TestUtilsLike;
import com.fernando.ms.likes.app.utils.TestUtilsUser;
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
public class LikePersistenceAdapterTest {
    @Mock
    private LikeReactiveMongoRepository likeReactiveMongoRepository;

    @Mock
    private LikePersistenceMapper likePersistenceMapper;

    @InjectMocks
    private LikePersistenceAdapter likePersistenceAdapter;


    @Test
    @DisplayName("When TargetId Is Correct Expect A List Likes Exists")
    void When_TargetIdIsCorrect_Expect_AListLikesExists() {
        Like like= TestUtilsLike.buildLikeMock();
        LikeDocument likeDocument=TestUtilsLike.buildLikeDocumentMock();
        when(likeReactiveMongoRepository.findAllByTargetId(anyString())).thenReturn(Flux.just(likeDocument));
        when(likePersistenceMapper.toLikes(any(Flux.class))).thenReturn(Flux.just(like));

        Flux<Like> result = likePersistenceAdapter.findAllByTargetId("67831b0ec8dda45d9a6c3022");

        StepVerifier.create(result)
                .expectNext(like)
                .verifyComplete();
        Mockito.verify(likeReactiveMongoRepository, times(1)).findAllByTargetId(anyString());
        Mockito.verify(likePersistenceMapper, times(1)).toLikes(any(Flux.class));
    }

    @Test
    @DisplayName("When Save Like Expect Like Saved Successfully")
    void when_SaveLike_Expect_LikeSavedSuccessfully() {
        LikeDocument likeDocument=TestUtilsLike.buildLikeDocumentMock();
        Like like= TestUtilsLike.buildLikeMock();
        when(likePersistenceMapper.toLikeDocument(any(Like.class))).thenReturn(likeDocument);
        when(likeReactiveMongoRepository.save(any(LikeDocument.class))).thenReturn(Mono.just(likeDocument));
        when(likePersistenceMapper.toLike(any(Mono.class))).thenReturn(Mono.just(like));

        Mono<Like> result = likePersistenceAdapter.save(like);

        StepVerifier.create(result)
                .expectNext(like)
                .verifyComplete();
        Mockito.verify(likeReactiveMongoRepository, times(1)).save(any(LikeDocument.class));
        Mockito.verify(likePersistenceMapper, times(1)).toLikeDocument(any(Like.class));
        Mockito.verify(likePersistenceMapper, times(1)).toLike(any(Mono.class));
    }

    @Test
    @DisplayName("When User and TargetType and TargetId Do Not Exist Expect Like Not Exists")
    void when_UserAndTargetTypeAndTargetIdDoNotExist_Expect_LikeNotExists() {
        User user = TestUtilsUser.buildUserMock();
        LikeUser likeUser = LikeUser.builder().userId(user.getId()).build();

        when(likePersistenceMapper.toLikeUser(any(User.class))).thenReturn(likeUser);
        when(likeReactiveMongoRepository.existsByLikeUserAndTargetTypeAndTargetId(any(LikeUser.class), anyString(), anyString())).thenReturn(Mono.just(false));

        Mono<Boolean> result = likePersistenceAdapter.existsByUserAndTargetTypeTargetId(user, "POST", "67831b0ec8dda45d9a6c3022");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("When User, TargetType, and TargetId Exist Expect Like Returned")
    void When_UserTargetTypeAndTargetIdExist_Expect_LikeReturned() {
        User user = TestUtilsUser.buildUserMock();
        Like like = TestUtilsLike.buildLikeMock();
        LikeDocument likeDocument = TestUtilsLike.buildLikeDocumentMock();
        LikeUser likeUser = LikeUser.builder().userId(user.getId()).build();

        when(likePersistenceMapper.toLikeUser(any(User.class))).thenReturn(likeUser);
        when(likeReactiveMongoRepository.findByLikeUserAndTargetTypeAndTargetId(any(LikeUser.class), anyString(), anyString())).thenReturn(Mono.just(likeDocument));
        when(likePersistenceMapper.toLike(any(LikeDocument.class))).thenReturn(like);

        Mono<Like> result = likePersistenceAdapter.findByLikeUserAndTargetTypeAndTargetId(user, "POST", "67831b0ec8dda45d9a6c3022");

        StepVerifier.create(result)
                .expectNext(like)
                .verifyComplete();
    }

    @Test
    @DisplayName("When Delete Is Successful Expect Void")
    void When_DeleteIsSuccessful_Expect_Void() {
        when(likeReactiveMongoRepository.deleteById(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = likePersistenceAdapter.delete("67831b0ec8dda45d9a6c3022");

        StepVerifier.create(result)
                .verifyComplete();
    }


}
