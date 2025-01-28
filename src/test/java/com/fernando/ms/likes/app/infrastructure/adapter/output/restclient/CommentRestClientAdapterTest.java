package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.CommentWebClient;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsCommentResponse;
import com.fernando.ms.likes.app.utils.TestUtilsComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentRestClientAdapterTest {
    @Mock
    private CommentWebClient commentWebClient;

    @InjectMocks
    private CommentRestClientAdapter commentRestClientAdapter;

    @Test
    @DisplayName("When CommentId Is Correct Expect True if Comment Exists")
    void When_CommentIdIsCorrect_Expect_TrueIfCommentExists(){
        when(commentWebClient.verify(anyString())).thenReturn(Mono.just(TestUtilsComment.buildExistsCommentResponseMock()));

        Mono<Boolean> exists=commentRestClientAdapter.verify("d2145sd");

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();
        Mockito.verify(commentWebClient,times(1)).verify(anyString());
    }

    @Test
    @DisplayName("When CommentId Is Not Correct Expect False if Comment Not Exists")
    void When_CommentIdIsNotCorrect_Expect_FalseIfCommentNotExists(){
        ExistsCommentResponse existsPostResponse=TestUtilsComment.buildExistsCommentResponseMock();
        existsPostResponse.setExists(false);
        when(commentWebClient.verify(anyString())).thenReturn(Mono.just(existsPostResponse));

        Mono<Boolean> exists=commentRestClientAdapter.verify("dsd235656ds");

        StepVerifier.create(exists)
                .expectNext(false)
                .verifyComplete();
        Mockito.verify(commentWebClient,times(1)).verify(anyString());
    }
}
