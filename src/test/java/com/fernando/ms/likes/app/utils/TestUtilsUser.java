package com.fernando.ms.likes.app.utils;

import com.fernando.ms.likes.app.domain.models.User;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;

public class TestUtilsUser {
    public static User buildUserMock(){
        return User.builder()
                .id(1L)
                .username("falx")
                .build();
    }

    public static ExistsUserResponse buildExistsUserResponseMock(){
        return ExistsUserResponse.builder()
                .exists(true)
                .build();
    }
}
