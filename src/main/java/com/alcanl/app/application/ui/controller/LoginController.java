package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.view.LoginForm;
import com.alcanl.app.application.ui.view.MainForm;
import com.alcanl.app.helper.Resources;
import com.alcanl.app.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class LoginController extends JFrame {

    @Value("${kismet.auto.stock.tracking.system.app.title}")
    private String m_appTitle;

    @Value("${kismet.auto.stock.tracking.system.app.frame.dimension.x}")
    private int m_frameStartDimensionX;

    @Value("${kismet.auto.stock.tracking.system.app.frame.dimension.y}")
    private int m_frameStartDimensionY;

    private final LoginForm m_loginForm;
    private final Resources m_resources;
    private final ExecutorService m_threadPool;
    private final JButton m_loginButton;
    private final JTextField m_usernameField;
    private final JPasswordField m_passwordField;
    private final UserService m_userService;


    public LoginController(Resources resources, ExecutorService threadPool, UserService userService)
    {
        m_loginForm = new LoginForm();
        m_loginButton = m_loginForm.getButtonLogin();
        m_usernameField = m_loginForm.getTextFieldUserName();
        m_passwordField = m_loginForm.getFieldPassword();
        m_resources = resources;
        m_threadPool = threadPool;
        m_userService = userService;
        initializeFrame();
        setOnViewListeners();
    }
    private void initializeWindowListener()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosed(e);
                m_threadPool.shutdown();
            }
        });
    }
    private void initializeFrame()
    {
        setContentPane(m_loginForm.getContentPane());
        setMinimumSize(new Dimension(m_frameStartDimensionX, m_frameStartDimensionY));
        setTitle(m_appTitle);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeWindowListener();
        pack();
        m_resources.setLayout(m_resources.nimbusTheme);
        m_resources.centerFrame(this);
    }
    private void buttonLoginOnClickListenerCallback(ActionEvent event) {
        var username = m_usernameField.getText();
        var password = String.valueOf(m_passwordField.getPassword());

        try {
            var userDtoOpt = m_threadPool.submit(
                    () -> m_userService.findUserByUsernameAndPassword(username, password))
                    .get(5, TimeUnit.SECONDS);

            if (userDtoOpt.isEmpty()) {
                m_usernameField.setText("");
                m_passwordField.setText("");
                m_resources.showNoSuchUserWarningDialog();
                m_usernameField.requestFocus();
            }
            else {
                this.setVisible(false);
                new MainForm();
            }
        } catch (ExecutionException | InterruptedException | TimeoutException ex) {
            m_resources.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    private void textFieldsOnEnterKeyClicked(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            m_loginButton.doClick();
    }

    private void setOnViewListeners()
    {
        m_loginButton.addActionListener(this::buttonLoginOnClickListenerCallback);
        m_passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textFieldsOnEnterKeyClicked(e);
            }
        });
        m_usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textFieldsOnEnterKeyClicked(e);
            }
        });
    }
}
