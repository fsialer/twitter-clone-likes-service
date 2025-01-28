package com.fernando.ms.likes.app.utils;

import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsCommentResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response.ExistsPostResponse;

public class TestUtilsComment {
    public static ExistsCommentResponse buildExistsCommentResponseMock(){
        return ExistsCommentResponse.builder()
                .exists(true)
                .build();
    }
}
