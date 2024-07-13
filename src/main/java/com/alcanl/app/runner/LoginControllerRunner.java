package com.alcanl.app.runner;

import com.alcanl.app.application.ui.controller.LoginController;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.awt.*;

@Order(1)
@Component
public class LoginControllerRunner implements ApplicationRunner
{
    private final LoginController m_controller;

    public LoginControllerRunner(LoginController controller)
    {
        m_controller = controller;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        EventQueue.invokeLater(() -> m_controller.setVisible(true));
    }
}
