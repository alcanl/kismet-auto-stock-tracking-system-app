package com.alcanl.app.application.ui.view.popup.notification;

import com.alcanl.app.application.ui.event.DisposeEvent;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;

@SwingContainer
@Component("bean.notification.critical.stock")
@Lazy
public class CriticalStockNotificationPopUp extends JDialog {
    private JPanel contentPane;
    private JButton buttonDispose;
    @Getter
    private JLabel labelMessage;
    @Getter
    @Setter
    @Accessors(prefix = "m_")
    private boolean m_isNotificationPopUpActive;

    public CriticalStockNotificationPopUp()
    {
        setContentPane(contentPane);
        setModal(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        pack();
        buttonDispose.addActionListener(e -> setVisible(false));
    }

    @PostConstruct
    private void init()
    {
        dispose();
    }

    @EventListener
    private void onEventReceived(DisposeEvent ignored)
    {
        dispose();
    }
}
