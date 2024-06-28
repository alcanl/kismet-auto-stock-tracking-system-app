package com.alcanl.app.ui.view;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;


@Component
public class LoginForm extends JFrame{
    private JPanel panelMain;
    private JLabel labelHeader;
    private JLabel labelUserName;
    private JTextField textFieldUserName;
    private JLabel labelPassword;
    private JPasswordField fieldPassword;

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
                ApplicationService.ms_threadPool.shutdown();
            }
        });*/
    }
}
