package com.fernando.ms.likes.app.infrastructure.adapter.input.rest;

import com.fernando.ms.likes.app.domain.exception.*;
import com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;

import static com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.likes.app.infrastructure.adapter.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.likes.app.infrastructure.utils.ErrorCatalog.*;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handleWebExchangeBindException(
            WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Mono.just(ErrorResponse.builder()
                .code(LIKE_BAD_PARAMETERS.getCode())
                .type(FUNCTIONAL)
                .message(LIKE_BAD_PARAMETERS.getMessage())
                .details(bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ErrorResponse> handleUserNotFoundException() {
        return Mono.just(ErrorResponse.builder()
                .code(USER_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(USER_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TargetTypeNotFoundException.class)
    public Mono<ErrorResponse> handleTargetTypeNotFoundException(TargetTypeNotFoundException e) {
        return Mono.just(ErrorResponse.builder()
                .code(TARGET_TYPE_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(TARGET_TYPE_NOT_FOUND.getMessage())
                        .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    public Mono<ErrorResponse> handlePostNotFoundException() {
        return Mono.just(ErrorResponse.builder()
                .code(POST_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(POST_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CommentNotFoundException.class)
    public Mono<ErrorResponse> handleCommentNotFoundException() {
        return Mono.just(ErrorResponse.builder()
                .code(COMMENT_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(COMMENT_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UniqueLikeException.class)
    public Mono<ErrorResponse> handleUniqueLikeException(UniqueLikeException e) {
        return Mono.just(ErrorResponse.builder()
                .code(UNIQUE_LIKE_RULE.getCode())
                .type(FUNCTIONAL)
                .message(UNIQUE_LIKE_RULE.getMessage())
                        .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LikeNotFoundException.class)
    public Mono<ErrorResponse> handleLikeNotFoundException() {
        return Mono.just(ErrorResponse.builder()
                .code(LIKE_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(LIKE_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build());
    }




    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handleException(Exception e) {
        log.info("Error:{} ", e.getMessage());
        return Mono.just(ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build());
    }
}
