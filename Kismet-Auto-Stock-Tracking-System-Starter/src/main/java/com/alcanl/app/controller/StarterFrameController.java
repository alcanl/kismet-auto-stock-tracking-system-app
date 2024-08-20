package com.alcanl.app.controller;

import com.alcanl.app.form.StarterForm;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SwingContainer
public class StarterFrameController extends JFrame {

    private Process m_process;
    private final StarterForm m_starterForm;
    private final ExecutorService m_threadPool;
    private final static String ms_title = "Kısmet Oto Stok Takip Sistemi";
    private final static String ms_warningTitle = "Uyarı";
    private final static String ms_warningMessage = "Alanlar Boş Bırakılamaz.";
    private final static String ms_errorTitle = "Hata";
    private final static String ms_errorMessageFromChild = "Failed to initialize JPA EntityManagerFactory";
    private final static String ms_successMessageFromParent = "Successfully_Started_Main_App";
    private final static String ms_mainAppPath = System.getenv("ProgramFiles(x86)") + "\\Kısmet Oto\\bin\\Kismet-Oto-Stock-Tracking-System-1.0.0.jar";
    private final static String ms_logoPath = System.getenv("ProgramFiles(x86)") + "\\Kısmet Oto\\assets\\default_logo.png";
    private final static String ms_errorMessage = "Veritabanı Bağlantı Hatası\nKullanıcı Adı / Parola Hatalı ya da Veritabanı Sunucuları Kapatılmış Olabilir";

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

    private void handleForgetFieldsCallback()
    {
        try {
            var documentsFolder = FileSystemView.getFileSystemView().getDefaultDirectory();
            var docFile = new File(documentsFolder, "KismetOto");
            var usernameFile = new File(docFile, "kismet_db_prop_un_.dat");
            var passwordFile = new File(docFile, "kismet_db_prop_p_.dat");

            Files.deleteIfExists(usernameFile.getAbsoluteFile().toPath());
            Files.deleteIfExists(passwordFile.getAbsoluteFile().toPath());
            Files.deleteIfExists(docFile.getAbsoluteFile().toPath());

        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String[] getDatabaseProps()
    {
        try {
            var docFile = new File(FileSystemView.getFileSystemView().getDefaultDirectory(), "KismetOto");
            var usernameFile = new File(docFile, "kismet_db_prop_un_.dat");
            var passwordFile = new File(docFile, "kismet_db_prop_p_.dat");

            if (!docFile.exists() || !usernameFile.exists() || !passwordFile.exists())
                return null;

            var fields = new String[2];

            try(var fis = new FileInputStream(usernameFile)) {
                fields[0] = new String(fis.readAllBytes());
            }

            try(var fis = new FileInputStream(passwordFile)) {
                fields[1] = new String(fis.readAllBytes());
            }

            return fields;

        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.WARNING, ex.getMessage());
            return null;
        }
    }
    private void handleRememberFieldsCallback(String username, String password)
    {
        try {
            var usernameBytes = username.getBytes();
            var passwordBytes = password.getBytes();
            var docFile = new File(FileSystemView.getFileSystemView().getDefaultDirectory(), "KismetOto");

            if (!docFile.exists())
                Files.createDirectory(docFile.getAbsoluteFile().toPath());

            var usernameFile = new File(docFile, "kismet_db_prop_un_.dat");
            var passwordFile = new File(docFile, "kismet_db_prop_p_.dat");

            Files.deleteIfExists(usernameFile.getAbsoluteFile().toPath());
            Files.deleteIfExists(passwordFile.getAbsoluteFile().toPath());

            try (var fos = new FileOutputStream(usernameFile)) {
                fos.write(usernameBytes);
            }
            try (var fos = new FileOutputStream(passwordFile)) {
                fos.write(passwordBytes);
            }

        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void registerKeys()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                m_threadPool.shutdownNow();
                if (m_process != null && m_process.isAlive())
                    m_process.destroy();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode())
                    m_starterForm.getButtonConnect().doClick();
            }
        });
        m_starterForm.getTextFieldDbUsername().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode())
                    m_starterForm.getButtonConnect().doClick();
            }
        });
        m_starterForm.getTextFieldDbPassword().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyCode())
                    m_starterForm.getButtonConnect().doClick();
            }
        });
    }
    private boolean areFieldsValid()
    {
        return m_starterForm.getTextFieldDbPassword().getPassword().length != 0 && !m_starterForm.getTextFieldDbUsername().getText().isBlank();
    }
    private void listenParentProcessMessageCallback(String username, String password)
    {
        try {
            var reader = new BufferedReader(new InputStreamReader(m_process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("Parent process received: " + line);
                if (line.contains(ms_errorMessageFromChild)) {
                    JOptionPane.showMessageDialog(null, ms_errorMessage, ms_errorTitle,
                            JOptionPane.ERROR_MESSAGE);
                    m_process.destroy();
                    break;
                }

                if (line.contains(ms_successMessageFromParent)) {
                    setVisible(false);
                    if (m_starterForm.getCheckBoxRememberFields().isSelected())
                        m_threadPool.execute(() -> handleRememberFieldsCallback(username, password));
                    else
                        m_threadPool.execute(this::handleForgetFieldsCallback);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
    private void startProcessListenerCallback()
    {
        while (true) {
            if (!m_process.isAlive()) {
                m_starterForm.getTextFieldDbUsername().setEditable(true);
                m_starterForm.getTextFieldDbPassword().setEditable(true);
                m_starterForm.getTextFieldDbPassword().setEnabled(true);
                m_starterForm.getTextFieldDbUsername().setEnabled(true);
                m_starterForm.getCheckBoxRememberFields().setEnabled(true);
                m_starterForm.getButtonConnect().setEnabled(true);
                SwingUtilities.invokeLater(() -> {
                    m_starterForm.getProgressBarLoading().setIndeterminate(false);
                    setVisible(true);
                });
                break;
            }
        }
    }
    private void buttonConnectClickedListener(ActionEvent event) {
        try {
            if (areFieldsValid()) {
                m_starterForm.getProgressBarLoading().setModel(new DefaultBoundedRangeModel());
                m_starterForm.getProgressBarLoading().setIndeterminate(true);
                var username = m_starterForm.getTextFieldDbUsername().getText().trim();
                var password = m_starterForm.getTextFieldDbPassword().getPassword();
                m_process = new ProcessBuilder("java", "-jar", ms_mainAppPath,
                        "--spring.datasource.username=%s".formatted(username),
                        "--spring.datasource.password=%s".formatted(String.valueOf(password)))
                        .redirectErrorStream(true).start();

                m_starterForm.getTextFieldDbUsername().setEditable(false);
                m_starterForm.getTextFieldDbPassword().setEditable(false);
                m_starterForm.getTextFieldDbPassword().setEnabled(false);
                m_starterForm.getTextFieldDbUsername().setEnabled(false);
                m_starterForm.getCheckBoxRememberFields().setEnabled(false);
                m_starterForm.getButtonConnect().setEnabled(false);
                m_threadPool.execute(this::startProcessListenerCallback);
                m_threadPool.execute(() -> listenParentProcessMessageCallback(username, String.valueOf(password)));

            }
            else
                JOptionPane.showMessageDialog(null, ms_warningMessage, ms_warningTitle,
                        JOptionPane.WARNING_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ms_errorTitle,
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public StarterFrameController(StarterForm starterForm)
    {
        m_starterForm = starterForm;
        m_threadPool = Executors.newFixedThreadPool(3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(m_starterForm.getPanelMain());
        setTitle(ms_title);
        setIconImage(new ImageIcon(ms_logoPath).getImage());
        setResizable(false);
        pack();
        centerFrame(this);
        m_starterForm.getButtonConnect().addActionListener(this::buttonConnectClickedListener);
        if (m_starterForm.getCheckBoxRememberFields().isSelected() && getDatabaseProps() != null) {
            m_starterForm.getTextFieldDbUsername().setText(getDatabaseProps()[0]);
            m_starterForm.getTextFieldDbPassword().setText(getDatabaseProps()[1]);
        }
        setOptionPaneButtonsTR();
        registerKeys();
        setVisible(true);
    }
}
