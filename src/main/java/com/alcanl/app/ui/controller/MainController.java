package com.alcanl.app.ui.controller;

import com.alcanl.app.ui.view.LoginForm;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.awt.*;

@Controller
public class MainController extends JFrame {
    private final LoginForm m_loginForm;

    public MainController(LoginForm loginForm)
    {

        m_loginForm = loginForm;
        setContentPane(m_loginForm.getContentPane());
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(300, 100));
        setSize(300, 100);
        setTitle("Kısmet Oto Kaporta");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

}
