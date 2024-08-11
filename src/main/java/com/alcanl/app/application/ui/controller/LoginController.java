package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.event.DisposeEvent;
import com.alcanl.app.application.ui.event.ShowLoginFormEvent;
import com.alcanl.app.application.ui.event.UserLoginEvent;
import com.alcanl.app.application.ui.view.form.LoginForm;
import static com.alcanl.app.helper.Resources.*;

import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.service.ApplicationService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private final LoginForm m_loginForm;
    private final DialogHelper m_dialogHelper;
    private final ExecutorService m_threadPool;
    private final ApplicationService m_applicationService;
    private final ApplicationEventPublisher m_applicationEventPublisher;
    private final ApplicationContext m_applicationContext;
    private final CurrentUserConfig m_currentUserConfig;

    public LoginController(ExecutorService threadPool, ApplicationService applicationService,
                           LoginForm loginForm, CurrentUserConfig currentUserConfig, ApplicationContext applicationContext,
                           DialogHelper dialogHelper, ApplicationEventPublisher applicationEventPublisher)
    {
        m_loginForm = loginForm;
        m_threadPool = threadPool;
        m_dialogHelper = dialogHelper;
        m_applicationContext = applicationContext;
        m_applicationEventPublisher = applicationEventPublisher;
        m_applicationService = applicationService;
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
        m_dialogHelper.setLayout();
        m_dialogHelper.initializeLogo(this);
        m_dialogHelper.centerFrame(this);
    }
    private void buttonLoginOnClickListenerCallback(ActionEvent event) {
        var username = m_loginForm.getTextFieldUserName().getText();
        var password = String.valueOf(m_loginForm.getFieldPassword().getPassword());

        try {
            var userDtoOpt = m_threadPool.submit(
                    () -> m_applicationService.findUserByUsernameAndPassword(username, password))
                    .get(5, TimeUnit.SECONDS);

            if (userDtoOpt.isEmpty()) {
                m_dialogHelper.showNoSuchUserWarningDialog();
                clearEditTexts();
            }
            else {
                clearEditTexts();
                setVisible(false);
                m_currentUserConfig.setUser(userDtoOpt.get());
                m_applicationContext.getBean("bean.frame.controller.main", MainFrameController.class)
                                .setVisible(true);
                m_applicationEventPublisher.publishEvent(new UserLoginEvent(this));

            }
        } catch (ExecutionException | InterruptedException | TimeoutException ex) {
            log.error(ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageDialog(ex.getMessage());
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
                    m_dialogHelper.setPasswordFieldVisibleOrInvisibleCallback(
                            m_loginForm.getFieldPassword(), m_loginForm.getLabelVisibility());
            }
        });
    }

    @EventListener
    @Async
    public void onApplicationShowFormEvent(ShowLoginFormEvent ignoredEvent)
    {
        m_currentUserConfig.setUser(null);
        setVisible(true);
    }
    @EventListener
    @Async
    public void onApplicationDisposeEvent(DisposeEvent ignoredEvent)
    {
        dispose();
    }
}
