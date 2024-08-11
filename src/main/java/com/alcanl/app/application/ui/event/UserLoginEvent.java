package com.alcanl.app.application.ui.event;

import org.springframework.context.ApplicationEvent;

public class UserLoginEvent extends ApplicationEvent {

    public UserLoginEvent(Object source) {
        super(source);
    }
}
