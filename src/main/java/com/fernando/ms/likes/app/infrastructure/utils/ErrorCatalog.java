package com.fernando.ms.likes.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {

    LIKE_BAD_PARAMETERS("LIKE_MS_001", "Invalid parameters for creation like"),
    INTERNAL_SERVER_ERROR("LIKE_MS_000", "Internal server error.");
    private final String code;
    private final String message;
}
