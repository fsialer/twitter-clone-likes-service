package com.fernando.ms.likes.app.infrastructure.adapter.output.persistence;

import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.mapper.LikePersistenceMapper;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeDocument;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.repository.LikeReactiveMongoRepository;
import com.fernando.ms.likes.app.utils.TestUtilsLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
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
}
