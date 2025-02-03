package com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateLikeRequest {
    @NotBlank(message = "Field targetId cannot be null or blank")
    private String targetId;
    @NotBlank(message = "Field targetType cannot be null or blank")
    private String targetType;
}
