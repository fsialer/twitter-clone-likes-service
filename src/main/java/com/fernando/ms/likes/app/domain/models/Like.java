package com.fernando.ms.likes.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Like {
    private String id;
    private String targetId;
    private String targetType;
    private User user;
}
