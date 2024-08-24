package com.alcanl.app.controller;

import com.alcanl.app.form.StarterForm;
import com.alcanl.app.process.child.database.DatabaseInitializerProcess;
import com.alcanl.app.process.child.main.MainAppInitializerProcess;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.alcanl.app.process.child.Common.*;

@SwingContainer
public class StarterFrameController extends JFrame {

    private final DatabaseInitializerProcess m_databaseInitializerProcess;
    private final MainAppInitializerProcess m_mainAppInitializerProcess;
    private final StarterForm m_starterForm;
    private final ExecutorService m_threadPool;


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
                if (m_databaseInitializerProcess.getDatabaseIntializerProcess() != null && m_databaseInitializerProcess.isProcessAlive())
                    m_databaseInitializerProcess.destroyProcess();

                if (m_mainAppInitializerProcess.getMainProcess() != null && m_mainAppInitializerProcess.isProcessAlive())
                    m_mainAppInitializerProcess.destroyProcess();
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
    private void listenMainAppProcessMessageCallback()
    {
        try {
            var reader = new BufferedReader(new InputStreamReader(
                    m_mainAppInitializerProcess.getMainProcess().getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("Parent process received: " + line);
                if (line.contains(ms_errorMessageFromMainChild)) {
                    JOptionPane.showMessageDialog(null, ms_errorMessage, ms_errorTitle,
                            JOptionPane.ERROR_MESSAGE);
                    m_mainAppInitializerProcess.destroyProcess();
                    break;
                }

                if (line.contains(ms_successMessageFromMainChild)) {
                    setVisible(false);
                    if (m_starterForm.getCheckBoxRememberFields().isSelected())
                        m_threadPool.execute(() -> handleRememberFieldsCallback(
                                m_mainAppInitializerProcess.getUsername(), m_mainAppInitializerProcess.getPassword()));
                    else
                        m_threadPool.execute(this::handleForgetFieldsCallback);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }

    private boolean listenDatabaseInitializerProcessMessageCallback()
    {
        try {
            var reader = new BufferedReader(new InputStreamReader(
                    m_databaseInitializerProcess.getDatabaseIntializerProcess().getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("Parent process received: " + line);
                if (line.contains(ms_errorMessageFromDatabaseChild)) {
                    JOptionPane.showMessageDialog(null, ms_errorMessage, ms_errorTitle,
                            JOptionPane.ERROR_MESSAGE);
                    m_databaseInitializerProcess.destroyProcess();
                    return false;
                }

                if (line.contains(ms_successMessageFromDatabaseChild) || line.contains(ms_successAlreadyMessageFromDatabaseChild)) {
                    m_databaseInitializerProcess.destroyProcess();
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        return false;
    }

    private void startMainAppProcessListenerCallback()
    {
        while (true) {
            if (!m_mainAppInitializerProcess.isProcessAlive()) {
                enableFields();
                break;
            }
        }
    }
    private void enableFields()
    {
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
    }

    private void startProcesses()
    {
        try {
            m_databaseInitializerProcess.startProcess();
            if (m_threadPool.submit(this::listenDatabaseInitializerProcessMessageCallback).get()) {
                m_mainAppInitializerProcess.startProcess();
                m_threadPool.execute(this::startMainAppProcessListenerCallback);
                m_threadPool.execute(this::listenMainAppProcessMessageCallback);
            }
            else {
                Logger.getLogger(StarterFrameController.class.getName()).log(Level.SEVERE, "Database initialization failed");
                enableFields();
            }

        } catch (IOException | ExecutionException | InterruptedException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), ms_errorTitle,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttonConnectClickedListener(ActionEvent event) {

        if (areFieldsValid()) {
            m_starterForm.getProgressBarLoading().setModel(new DefaultBoundedRangeModel());
            m_starterForm.getProgressBarLoading().setIndeterminate(true);
            var username = m_starterForm.getTextFieldDbUsername().getText().trim();
            var password = String.valueOf(m_starterForm.getTextFieldDbPassword().getPassword());
            m_databaseInitializerProcess.setUsername(username);
            m_databaseInitializerProcess.setPassword(password);
            m_mainAppInitializerProcess.setUsername(username);
            m_mainAppInitializerProcess.setPassword(password);
            m_threadPool.execute(this::startProcesses);

            m_starterForm.getTextFieldDbUsername().setEditable(false);
            m_starterForm.getTextFieldDbPassword().setEditable(false);
            m_starterForm.getTextFieldDbPassword().setEnabled(false);
            m_starterForm.getTextFieldDbUsername().setEnabled(false);
            m_starterForm.getCheckBoxRememberFields().setEnabled(false);
            m_starterForm.getButtonConnect().setEnabled(false);
        }
        else
            JOptionPane.showMessageDialog(null, ms_warningMessage, ms_warningTitle,
                    JOptionPane.WARNING_MESSAGE);
    }


    public StarterFrameController(StarterForm starterForm)
    {
        m_starterForm = starterForm;
        m_threadPool = Executors.newCachedThreadPool();
        m_databaseInitializerProcess = new DatabaseInitializerProcess();
        m_mainAppInitializerProcess = new MainAppInitializerProcess();
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
