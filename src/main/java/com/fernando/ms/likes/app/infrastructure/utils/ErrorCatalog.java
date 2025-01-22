package com.fernando.ms.likes.app.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {

    LIKE_BAD_PARAMETERS("LIKE_MS_001", "Invalid parameters for creation like"),
    USER_NOT_FOUND("LIKE_MS_002", "User not found."),
    POST_NOT_FOUND("LIKE_MS_003", "Post not found."),
    COMMENT_NOT_FOUND("LIKE_MS_004", "Comment not found."),
    TARGET_TYPE_NOT_FOUND("LIKE_MS_005", "Target type not found. "),
    UNIQUE_LIKE_RULE("LIKE_MS_006", "Violation constraint rule: "),
    LIKE_NOT_FOUND("LIKE_MS_007", "Like not found."),
    INTERNAL_SERVER_ERROR("LIKE_MS_000", "Internal server error.");
    private final String code;
    private final String message;
}
