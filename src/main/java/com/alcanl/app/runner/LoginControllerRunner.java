package com.alcanl.app.runner;

import com.alcanl.app.application.ui.controller.LoginController;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.awt.*;

@Order(2)
@Component
public class LoginControllerRunner implements ApplicationRunner
{
    private final LoginController m_loginController;

    public LoginControllerRunner(LoginController loginController)
    {
        m_loginController = loginController;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        EventQueue.invokeLater(() -> m_loginController.setVisible(true));
    }
}
