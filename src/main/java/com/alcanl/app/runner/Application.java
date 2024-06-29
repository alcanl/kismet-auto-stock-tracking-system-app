package com.alcanl.app.runner;

import com.alcanl.app.application.ui.controller.MainController;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.awt.*;


@Component
public class Application implements ApplicationRunner
{
    private final MainController m_controller;

    public Application(MainController controller)
    {
        m_controller = controller;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        EventQueue.invokeLater(() -> m_controller.setVisible(true));
    }
}
