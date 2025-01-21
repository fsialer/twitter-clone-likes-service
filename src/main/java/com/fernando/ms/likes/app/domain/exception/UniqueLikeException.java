package com.fernando.ms.likes.app.domain.exception;

public class UniqueLikeException extends RuntimeException{
    public UniqueLikeException(String msg){
        super(msg);
    }
}
