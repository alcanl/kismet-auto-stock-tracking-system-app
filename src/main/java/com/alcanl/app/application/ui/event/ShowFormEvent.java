package com.alcanl.app.application.ui.event;

import org.springframework.context.ApplicationEvent;

public class ShowFormEvent extends ApplicationEvent {

    public ShowFormEvent(Object source)
    {
        super(source);
    }
}
