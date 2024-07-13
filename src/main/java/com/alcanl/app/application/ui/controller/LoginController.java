package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.view.LoginForm;
import com.alcanl.app.helper.Resources;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;

@Controller
public class LoginController extends JFrame {
    private final LoginForm m_loginForm;

    public LoginController()
    {
        m_loginForm = new LoginForm();
        setContentPane(m_loginForm.getContentPane());
        setMinimumSize(new Dimension(600, 400));
        Resources.centerFrame(this);
        setTitle("Kısmet Oto Kaporta");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

}
