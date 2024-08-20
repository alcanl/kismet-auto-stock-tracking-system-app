package com.alcanl.app.application.ui.event;

import org.springframework.context.ApplicationEvent;

public class ShowLoginFormEvent extends ApplicationEvent {

    public ShowLoginFormEvent(Object source)
    {
        super(source);
    }
}
