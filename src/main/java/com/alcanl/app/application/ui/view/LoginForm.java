package com.alcanl.app.application.ui.view;

import lombok.Getter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
@Order(0)
public class LoginForm extends JFrame{
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

    public LoginForm()
    {
        setContentPane(panelMain);
        setSize(1400, 800);
        setTitle("Kısmet Oto Kaporta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));
    }
}
