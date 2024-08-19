package com.alcanl.app.controller;

import com.alcanl.app.form.StarterForm;

import static com.google.common.io.Resources.getResource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SwingContainer
public class StarterFrameController extends JFrame {
    private final StarterForm m_starterForm;
    private final ScheduledExecutorService m_threadPool;
    private final static String ms_title = "Kısmet Oto Stok Takip Sistemi";
    private final static String ms_warningTitle = "Uyarı";
    private final static String ms_warningMessage = "Alanlar Boş Bırakılamaz.";
    private final static String ms_errorTitle = "Hata";

    private static void setOptionPaneButtonsTR()
    {
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
        UIManager.put("OptionPane.cancelButtonText", "İptal");
        UIManager.put("OptionPane.okButtonText", "Tamam");
    }
    private void centerFrame(JFrame frame)
    {
        var x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - frame.getSize().width / 2;
        var y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - frame.getSize().height / 2;
        frame.setLocation(x, y);
    }

    public StarterFrameController(StarterForm starterForm)
    {
        m_starterForm = starterForm;
        m_threadPool = Executors.newScheduledThreadPool(2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(m_starterForm.getPanelMain());
        setTitle(ms_title);
        setResizable(false);
        pack();
        centerFrame(this);
        m_starterForm.getButtonConnect().addActionListener(this::buttonConnectClickedListener);
        setOptionPaneButtonsTR();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                m_threadPool.shutdownNow();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode())
                    m_starterForm.getButtonConnect().doClick();
            }
        });
    }
    private boolean areFieldsValid()
    {
        return !m_starterForm.getTextFieldDbPassword().getText().isBlank() && !m_starterForm.getTextFieldDbUsername().getText().isBlank();
    }
    private void startProcessListenerCallback(Process process)
    {
        while (true) {
            if (!process.isAlive()) {
                m_starterForm.getTextFieldDbUsername().setEditable(true);
                m_starterForm.getTextFieldDbPassword().setEditable(true);
                m_starterForm.getTextFieldDbPassword().setEnabled(true);
                m_starterForm.getTextFieldDbUsername().setEnabled(true);
                m_starterForm.getButtonConnect().setEnabled(true);
                SwingUtilities.invokeLater(() -> m_starterForm.getProgressBarLoading().setIndeterminate(false));
                setVisible(true);
                return;
            }
        }
    }
    private void buttonConnectClickedListener(ActionEvent event) {
        try {
            if (areFieldsValid()) {
                m_starterForm.getProgressBarLoading().setModel(new DefaultBoundedRangeModel());
                m_starterForm.getProgressBarLoading().setIndeterminate(true);
                var username = m_starterForm.getTextFieldDbUsername().getText().trim();
                var password = m_starterForm.getTextFieldDbPassword().getText().trim();
                var process = new ProcessBuilder("java", "-jar",
                        getResource("Kismet-Oto-Stock-Tracking-System-1.0.0.jar")
                        .getPath().substring(1),
                        "spring.datasource.username=%s".formatted(username),
                        "spring.datasource.password=%s".formatted(password))
                        .inheritIO().start();

                m_starterForm.getTextFieldDbUsername().setEditable(false);
                m_starterForm.getTextFieldDbPassword().setEditable(false);
                m_starterForm.getTextFieldDbPassword().setEnabled(false);
                m_starterForm.getTextFieldDbUsername().setEnabled(false);
                m_starterForm.getButtonConnect().setEnabled(false);
                m_threadPool.execute(() -> startProcessListenerCallback(process));
                m_threadPool.schedule(() -> setVisible(false), 20, TimeUnit.SECONDS);

            } else
                JOptionPane.showMessageDialog(null, ms_warningMessage, ms_warningTitle,
                        JOptionPane.WARNING_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ms_errorTitle,
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
