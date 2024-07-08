package com.alcanl.app.repository.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductAlreadyExistException extends RuntimeException{
    public ProductAlreadyExistException(String message) {
        super(message);
    }
}
