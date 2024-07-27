package com.alcanl.app.application.ui.event;

import org.springframework.context.ApplicationEvent;

public class UpdateTablesEvent extends ApplicationEvent {
    public UpdateTablesEvent(Object source)
    {
        super(source);
    }
}
