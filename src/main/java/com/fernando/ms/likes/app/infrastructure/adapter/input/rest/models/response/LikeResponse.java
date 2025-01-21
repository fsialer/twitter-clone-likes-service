package com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponse {
    private String id;
    private String targetId;
    private String targetType;
}
