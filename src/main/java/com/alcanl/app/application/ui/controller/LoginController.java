package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.view.LoginForm;
import static com.alcanl.app.helper.Resources.*;
import com.alcanl.app.helper.Resources;
import com.alcanl.app.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Controller
public class LoginController extends JFrame {

    @Value("${kismet.auto.stock.tracking.system.app.title}")
    private String m_appTitle;

    @Value("${kismet.auto.stock.tracking.system.app.frame.login.dimension.x}")
    private int m_frameStartDimensionX;

    @Value("${kismet.auto.stock.tracking.system.app.frame.login.dimension.y}")
    private int m_frameStartDimensionY;

    @Value("${kismet.auto.stock.tracking.system.app.icon.visible.path}")
    private String m_setPasswordVisibleIconPath;

    @Value("${kismet.auto.stock.tracking.system.app.icon.hidden.path}")
    private String m_setPasswordHiddenIconPath;

    private final LoginForm m_loginForm;
    private final Resources m_resources;
    private final ExecutorService m_threadPool;
    private final UserService m_userService;
    private final ApplicationContext m_applicationContext;
    private final MainFrameController m_mainFrameController;

    public LoginController(Resources resources, ExecutorService threadPool, UserService userService,
                           ApplicationContext applicationContext, LoginForm loginForm, MainFrameController mainFrameController)
    {
        m_loginForm = loginForm;
        m_resources = resources;
        m_threadPool = threadPool;
        m_userService = userService;
        m_applicationContext = applicationContext;
        m_mainFrameController = mainFrameController;
        initializeFrame();
        setOnViewListeners();
    }

    @PostConstruct
    private void setFormTitle()
    {
        setTitle(m_appTitle);
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
        setContentPane(m_loginForm.getPanelMain());
        setMinimumSize(new Dimension(m_frameStartDimensionX, m_frameStartDimensionY));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeWindowListener();
        pack();
        m_resources.setLayout();
        m_resources.initializeLogo(this);
        m_resources.centerFrame(this);
    }
    private void buttonLoginOnClickListenerCallback(ActionEvent event) {
        var username = m_loginForm.getTextFieldUserName().getText();
        var password = String.valueOf(m_loginForm.getFieldPassword().getPassword());

        try {
            var userDtoOpt = m_threadPool.submit(
                    () -> m_userService.findUserByUsernameAndPassword(username, password))
                    .get(5, TimeUnit.SECONDS);

            if (userDtoOpt.isEmpty()) {
                m_loginForm.getTextFieldUserName().setText(EMPTY_STRING);
                m_loginForm.getFieldPassword().setText(EMPTY_STRING);
                m_resources.showNoSuchUserWarningDialog();
                m_loginForm.getTextFieldUserName().requestFocus();
            }
            else {
                this.setVisible(false);
                m_mainFrameController.setVisible(true);

            }
        } catch (ExecutionException | InterruptedException | TimeoutException ex) {
            m_resources.showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    private void textFieldsOnEnterKeyClicked(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            m_loginForm.getButtonLogin().doClick();
    }

    private void setOnViewListeners()
    {
        m_loginForm.getButtonLogin().addActionListener(this::buttonLoginOnClickListenerCallback);
        m_loginForm.getFieldPassword().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textFieldsOnEnterKeyClicked(e);
            }
        });
        m_loginForm.getTextFieldUserName().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textFieldsOnEnterKeyClicked(e);
            }
        });
        m_loginForm.getLabelVisibility().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    setPasswordFieldVisibleOrInvisibleCallback();
            }
        });
    }
    private void setPasswordFieldVisibleOrInvisibleCallback()
    {
        try {
            if (m_loginForm.getFieldPassword().echoCharIsSet()) {
                m_loginForm.getFieldPassword().setEchoChar('\0');
                m_loginForm.getLabelVisibility().setIcon(
                        new ImageIcon(m_applicationContext.getResource(m_setPasswordVisibleIconPath).getContentAsByteArray()));
            }
            else {
                m_loginForm.getFieldPassword().setEchoChar('*');
                m_loginForm.getLabelVisibility().setIcon(
                        new ImageIcon(m_applicationContext.getResource(m_setPasswordHiddenIconPath).getContentAsByteArray()));
            }
        } catch (IOException ex) {
            log.error("setPasswordFieldVisibleOrInvisibleCallback", ex);
            m_resources.showUnknownErrorMessageDialog(ex.getMessage());
        }

    }
}
