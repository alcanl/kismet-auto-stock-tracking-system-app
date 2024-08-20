package com.alcanl.app.controller;

import com.alcanl.app.form.StarterForm;

import static com.google.common.io.Resources.getResource;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SwingContainer
public class StarterFrameController extends JFrame {
    private final StarterForm m_starterForm;
    private final ExecutorService m_threadPool;
    private Process m_process;
    private final static String ms_title = "Kısmet Oto Stok Takip Sistemi";
    private final static String ms_successMessage = "Successfully_Started_Main_App";
    private final static String ms_warningTitle = "Uyarı";
    private final static String ms_warningMessage = "Alanlar Boş Bırakılamaz.";
    private final static String ms_errorTitle = "Hata";
    private final static String ms_errorTitleEN = "ERROR";
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

    private void handleForgetFields()
    {
        try {
            var fileSystemView = FileSystemView.getFileSystemView();
            var documentsFolder = fileSystemView.getDefaultDirectory();
            var docDirectory = new File(documentsFolder, "KismetOto");
            var fileUsername = new File(docDirectory, "kismet_db_props_un.dat");
            var filePassword = new File(docDirectory, "kismet_db_props_p.dat");

            Files.deleteIfExists(fileUsername.getAbsoluteFile().toPath());
            Files.deleteIfExists(filePassword.getAbsoluteFile().toPath());
            Files.deleteIfExists(docDirectory.getAbsoluteFile().toPath());

        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleRememberFields(String username, String password)
    {
        try {
            var userNameBytes = username.getBytes();
            var passwordBytes = password.getBytes();
            var fileSystemView = FileSystemView.getFileSystemView();
            var documentsFolder = fileSystemView.getDefaultDirectory();
            var docDirectory = new File(documentsFolder, "KismetOto");

            if (!docDirectory.exists())
                Files.createDirectory(docDirectory.getAbsoluteFile().toPath());

            var fileUsername = new File(docDirectory, "kismet_db_props_un.dat");
            var filePassword = new File(docDirectory, "kismet_db_props_p.dat");

            Files.deleteIfExists(fileUsername.getAbsoluteFile().toPath());
            Files.deleteIfExists(filePassword.getAbsoluteFile().toPath());

            try (var fos = new FileOutputStream(fileUsername)) {
                fos.write(userNameBytes);
            }
            try (var fos = new FileOutputStream(filePassword)) {
                fos.write(passwordBytes);
            }
        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String[] readDatabaseProps()
    {
        String[] props = new String[2];

        try {
            var fileSystemView = FileSystemView.getFileSystemView();
            var documentsFolder = fileSystemView.getDefaultDirectory();
            var docDirectory = new File(documentsFolder, "KismetOto");
            if (!docDirectory.exists())
                return null;

            var fileUsername = new File(docDirectory, "kismet_db_props_un.dat");
            var filePassword = new File(docDirectory, "kismet_db_props_p.dat");

            try (var fis = new FileInputStream(fileUsername)) {
                props[0] = new String(fis.readAllBytes());
            }

            try (var fis = new FileInputStream(filePassword)) {
                props[1] = new String(fis.readAllBytes());
            }

            return props;
        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void listenMessageFromChildCallback(String username, String password)
    {
        try {
            var reader = new BufferedReader(new InputStreamReader(m_process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains(ms_successMessage)) {
                    setVisible(false);
                    if (m_starterForm.getCheckBoxRememberFields().isSelected())
                        m_threadPool.execute(() -> handleRememberFields(username, password));
                    else
                        m_threadPool.execute(this::handleForgetFields);
                    return;
                }
                if (line.contains(ms_errorTitleEN)){
                    JOptionPane.showMessageDialog(null, ms_errorMessage, ms_errorTitle,
                            JOptionPane.ERROR_MESSAGE);
                    m_process.destroy();
                    return;
                }
            }
        } catch (Throwable ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public StarterFrameController(StarterForm starterForm)
    {
        m_starterForm = starterForm;
        m_threadPool = Executors.newFixedThreadPool(3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(m_starterForm.getPanelMain());
        setTitle(ms_title);
        setIconImage(new ImageIcon(getResource("default_logo.png").getPath()).getImage());
        setResizable(false);
        pack();
        centerFrame(this);
        m_starterForm.getButtonConnect().addActionListener(this::buttonConnectClickedListener);
        setOptionPaneButtonsTR();
        if (m_starterForm.getCheckBoxRememberFields().isSelected() && readDatabaseProps() != null) {
            m_starterForm.getTextFieldDbUsername().setText(Objects.requireNonNull(readDatabaseProps())[0]);
            m_starterForm.getTextFieldDbPassword().setText(Objects.requireNonNull(readDatabaseProps())[1]);
        }

        setVisible(true);
        registerKeys();
    }
    private void registerKeys()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                m_threadPool.shutdownNow();

                if (m_process!= null && m_process.isAlive())
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
    private void startProcessListenerCallback()
    {
        while (true) {
            if (!m_process.isAlive()) {
                m_starterForm.getTextFieldDbUsername().setEditable(true);
                m_starterForm.getTextFieldDbPassword().setEditable(true);
                m_starterForm.getTextFieldDbPassword().setEnabled(true);
                m_starterForm.getTextFieldDbUsername().setEnabled(true);
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

                m_process = new ProcessBuilder("java", "-jar",
                        getResource("Kismet-Oto-Stock-Tracking-System-1.0.0.jar")
                                .getPath().substring(1),
                        "--spring.datasource.username=%s".formatted(username),
                        "--spring.datasource.password=%s".formatted(String.valueOf(password)))
                        .redirectErrorStream(true).start();

                m_starterForm.getTextFieldDbUsername().setEditable(false);
                m_starterForm.getTextFieldDbPassword().setEditable(false);
                m_starterForm.getTextFieldDbPassword().setEnabled(false);
                m_starterForm.getTextFieldDbUsername().setEnabled(false);
                m_starterForm.getButtonConnect().setEnabled(false);
                m_threadPool.execute(this::startProcessListenerCallback);
                m_threadPool.execute(() -> listenMessageFromChildCallback(username, String.valueOf(password)));

            } else
                JOptionPane.showMessageDialog(null, ms_warningMessage, ms_warningTitle,
                        JOptionPane.WARNING_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ms_errorTitle,
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
