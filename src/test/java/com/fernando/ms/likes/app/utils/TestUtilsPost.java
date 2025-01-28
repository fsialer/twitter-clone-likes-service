package com.fernando.ms.likes.app.utils;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsPostResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsUserResponse;

public class TestUtilsPost {
    public static ExistsPostResponse buildExistsPostResponseMock(){
        return ExistsPostResponse.builder()
                .exists(true)
                .build();
    }
}
