package com.alcanl.app.application.ui.event;

import org.springframework.context.ApplicationEvent;

public class DisposeEvent extends ApplicationEvent {
    public DisposeEvent(Object source) {
        super(source);
    }
}
