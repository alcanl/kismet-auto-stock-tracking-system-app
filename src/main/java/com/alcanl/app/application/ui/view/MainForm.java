package com.alcanl.app.application.ui.view;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MainForm extends JFrame {
    @Getter
    private JPanel panelMain;
    private JPanel panelBottomBar;
    private JLabel labelVersion;
    @Getter
    private JLabel labelWelcomeUser;
    private JPanel panelTopBar;

}
