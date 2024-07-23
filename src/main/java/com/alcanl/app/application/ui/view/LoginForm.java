package com.alcanl.app.application.ui.view;

import lombok.Getter;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("ALL")
@Component
public class LoginForm extends JFrame{
    @Getter
    private JPanel panelMain;
    private JLabel labelLogo;
    private JLabel labelUserName;
    @Getter
    private JTextField textFieldUserName;
    private JLabel labelPassword;
    @Getter
    private JPasswordField fieldPassword;
    private JPanel panelLogo;
    private JPanel panelUser;
    @Getter
    private JButton buttonLogin;
    private JPanel panelLogin;
    private JLabel labelVersion;
    private JLabel labelRights;
    @Getter
    private JLabel labelVisibility;
    private JLabel labelBalance;

    public LoginForm()
    {
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));
    }
}
