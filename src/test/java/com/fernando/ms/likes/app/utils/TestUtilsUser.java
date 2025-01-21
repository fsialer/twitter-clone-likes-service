package com.fernando.ms.likes.app.utils;

import com.fernando.ms.likes.app.domain.models.User;

public class TestUtilsUser {
    public static User buildUserMock(){
        return User.builder()
                .id(1L)
                .username("falx")
                .build();
    }
}
