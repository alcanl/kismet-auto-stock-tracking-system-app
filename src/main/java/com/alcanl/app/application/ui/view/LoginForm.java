package com.alcanl.app.application.ui.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
@Scope("lazy")
public class LoginForm extends JFrame{
    private JPanel panelMain;
    private JLabel labelLogo;
    private JLabel labelUserName;
    private JTextField textFieldUserName;
    private JLabel labelPassword;
    private JPasswordField fieldPassword;
    private JPanel panelLogo;
    private JPanel panelUser;
    private JButton buttonLogin;
    private JPanel panelLogin;
    private JLabel labelVersion;
    private JLabel labelRights;

    public LoginForm()
    {
        //Resources.setLayout(NIMBUS_THEME);
        setContentPane(panelMain);
        setSize(1400, 800);
        setTitle("Kısmet Oto Kaporta");
        //setIconImage(Toolkit.getDefaultToolkit().createImage(getResource(DEFAULT_ICON)));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 800));

        /*addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosed(e);
                executorService.shutdown();
                UserService.ms_threadPool.shutdown();
            }
        });*/

        textFieldUserName.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("TEST");
                }
            }
        });
    }
}
