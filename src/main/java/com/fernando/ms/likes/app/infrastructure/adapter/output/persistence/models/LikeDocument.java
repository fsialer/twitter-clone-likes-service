package com.fernando.ms.likes.app.infrastructure.adapter.output.persistence.models;

import com.fernando.ms.likes.app.domain.models.User;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "likes")
public class LikeDocument {
    private String id;
    private String targetId;
    private String targetType;
    private LikeUser likeUser;
    private LocalDateTime createdAt;
}
