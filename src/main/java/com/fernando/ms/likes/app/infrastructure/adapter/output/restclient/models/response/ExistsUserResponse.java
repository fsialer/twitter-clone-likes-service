package com.fernando.ms.likes.app.infrastructure.adapter.output.restclient.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExistsUserResponse {
    private Boolean exists;
}
