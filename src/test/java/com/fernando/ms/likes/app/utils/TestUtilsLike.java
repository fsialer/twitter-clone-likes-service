package com.fernando.ms.likes.app.utils;

import com.fernando.ms.likes.app.domain.models.Like;
import com.fernando.ms.likes.app.domain.models.User;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request.CreateLikeRequest;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.LikeResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.QuantityLikeResponse;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeDocument;
import com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models.LikeUser;

public class TestUtilsLike {

    public static String buildTargetIdMock(){
        return "67831b0ec8dda45d9a6c3022";
    }

    public static String buildTargetTypeMock(){
        return "POST";
    }

    public static Like buildLikeMock(){
        return Like.builder()
                .id("6786e1d320e176064b92acd9")
                .user(User.builder()
                        .id(1L)
                        .build())
                .targetId("67831b0ec8dda45d9a6c3022")
                .targetType("POST")
                .build();
    }

    public static LikeDocument buildLikeDocumentMock(){
        return LikeDocument.builder()
                .id("6786e1d320e176064b92acd9")
                .likeUser(LikeUser.builder()
                        .userId(1L)
                        .build())
                .targetId("67831b0ec8dda45d9a6c3022")
                .targetType("POST")
                .build();
    }

    public static QuantityLikeResponse buildQuantityLikeResponseMock(){
        return QuantityLikeResponse.builder()
                .quantity(1L)
                .build();
    }

    public static CreateLikeRequest buildCreateLikeRequestMock(){
        return CreateLikeRequest.builder()
                .targetId("67831b0ec8dda45d9a6c3022")
                .targetType("POST")
                .build();
    }

    public static LikeResponse buildLikeResponseMock(){
        return LikeResponse.builder()
                .id("1")
                .targetId("67831b0ec8dda45d9a6c3022")
                .targetType("POST")
                .build();
    }







}
