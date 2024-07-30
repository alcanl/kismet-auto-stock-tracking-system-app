package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.event.DisposeEvent;
import com.alcanl.app.application.ui.event.ShowFormEvent;
import com.alcanl.app.application.ui.view.form.LoginForm;
import static com.alcanl.app.helper.Resources.*;

import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.helper.Resources;
import com.alcanl.app.service.ApplicationService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.*;

@Slf4j
@Controller
@DependsOn({"bean.form.login", "bean.form.main", "bean.dialog.add.new.product"})
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
    private final ApplicationService m_applicationService;
    private final ApplicationContext m_applicationContext;
    private final MainFrameController m_mainFrameController;
    private final CurrentUserConfig m_currentUserConfig;

    public LoginController(Resources resources, ExecutorService threadPool, ApplicationService applicationService,
                           ApplicationContext applicationContext, LoginForm loginForm, MainFrameController mainFrameController,
                           CurrentUserConfig currentUserConfig)
    {
        m_loginForm = loginForm;
        m_resources = resources;
        m_threadPool = threadPool;
        m_applicationService = applicationService;
        m_applicationContext = applicationContext;
        m_mainFrameController = mainFrameController;
        m_currentUserConfig = currentUserConfig;
    }

    @PostConstruct
    private void setFormTitle()
    {
        initializeFrame();
        setOnViewListeners();
        setTitle(m_appTitle);
    }

    private void clearEditTexts()
    {
        m_loginForm.getTextFieldUserName().setText(EMPTY_STRING);
        m_loginForm.getFieldPassword().setText(EMPTY_STRING);
        m_loginForm.getTextFieldUserName().requestFocus();
    }

    private void initializeWindowListener()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosed(e);
                m_threadPool.shutdownNow();
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
                    () -> m_applicationService.findUserByUsernameAndPassword(username, password))
                    .get(5, TimeUnit.SECONDS);

            if (userDtoOpt.isEmpty()) {
                m_resources.showNoSuchUserWarningDialog();
                clearEditTexts();
            }
            else {
                clearEditTexts();
                this.setVisible(false);
                m_currentUserConfig.setUser(userDtoOpt.get());
                m_mainFrameController.setVisible(true);
            }
        } catch (ExecutionException | InterruptedException | TimeoutException ex) {
            log.error(ex.getMessage());
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
    @EventListener
    @Async
    public void onApplicationShowFormEvent(ShowFormEvent ignoredEvent)
    {
        m_currentUserConfig.setUser(null);
        setVisible(true);
    }
    @EventListener
    @Async
    public void onApplicationDisposeEvent(DisposeEvent ignoredEvent)
    {
        this.dispose();
    }
}
