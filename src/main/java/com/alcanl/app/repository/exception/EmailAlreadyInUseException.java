package com.alcanl.app.repository.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message)
    {
        super(message);
    }
}
