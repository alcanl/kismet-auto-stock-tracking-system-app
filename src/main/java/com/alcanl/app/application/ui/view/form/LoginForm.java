package com.alcanl.app.application.ui.view.form;

import lombok.Getter;
import javax.swing.*;

@SuppressWarnings("ALL")
public class LoginForm extends JFrame {
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
}
