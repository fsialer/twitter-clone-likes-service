package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.client.UserWebClient;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;
import com.fernando.ms.likes.app.utils.TestUtilsUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRestClientAdapterTest {

    @Mock
    private UserWebClient userWebClient;

    @InjectMocks
    private UserRestClientAdapter userRestClientAdapter;


    @Test
    @DisplayName("When UserId Is Correct Expect True if User Exists")
    void When_UserIdIsCorrect_Expect_TrueIfUserExists(){
        when(userWebClient.verify(anyLong())).thenReturn(Mono.just(TestUtilsUser.buildExistsUserResponseMock()));

        Mono<Boolean> exists=userRestClientAdapter.verify(1L);

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).verify(anyLong());
    }

    @Test
    @DisplayName("When UserId Is Not Correct Expect False if User Exists")
    void When_UserIdIsNotCorrect_Expect_FalseIfUserExists(){
        ExistsUserResponse existsUserResponse=TestUtilsUser.buildExistsUserResponseMock();
        existsUserResponse.setExists(false);
        when(userWebClient.verify(anyLong())).thenReturn(Mono.just(existsUserResponse));

        Mono<Boolean> exists=userRestClientAdapter.verify(1L);

        StepVerifier.create(exists)
                .expectNext(false)
                .verifyComplete();
        Mockito.verify(userWebClient,times(1)).verify(anyLong());
    }
}
