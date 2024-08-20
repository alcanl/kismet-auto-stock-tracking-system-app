package com.alcanl.app.repository.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsernameAlreadyInUseException extends RuntimeException {
    public UsernameAlreadyInUseException(String message)
    {
        super(message);
    }
}
