package com.alcanl.app.application.ui.controller;

import com.alcanl.app.application.ui.view.MainForm;
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
import java.util.concurrent.ExecutorService;

@Slf4j
@Controller
public class MainFrameController extends JFrame {

    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.x}")
    private int m_mainFrameStartDimensionX;

    @Value("${kismet.auto.stock.tracking.system.app.frame.main.dimension.y}")
    private int m_mainFrameStartDimensionY;

    private final MainForm m_mainForm;
    private final Resources m_resources;
    private final ExecutorService m_threadPool;
    private final UserService m_userService;
    private final ApplicationContext m_applicationContext;

    public MainFrameController(Resources resources, ExecutorService threadPool, UserService userService,
                               ApplicationContext applicationContext, MainForm mainForm)
    {
        m_mainForm = mainForm;
        m_resources = resources;
        m_threadPool = threadPool;
        m_userService = userService;
        m_applicationContext = applicationContext;
        initializeFrame();
    }

    @PostConstruct
    private void setFrameSize()
    {
        setMinimumSize(new Dimension(m_mainFrameStartDimensionX, m_mainFrameStartDimensionY));
        setSize(new Dimension(m_mainFrameStartDimensionX, m_mainFrameStartDimensionY));
        m_resources.centerFrame(this);
        var text = m_mainForm.getLabelWelcomeUser().getText();
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
        setContentPane(m_mainForm.getPanelMain());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeWindowListener();
        setUndecorated(true);
        m_resources.setLayout();
        m_resources.initializeLogo(this);
    }
}
