package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.*;

@SwingContainer
@Slf4j
@Component("bean.dialog.fast.stock")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
public class DialogFastStockAddition extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<ProductDTO> listProductName;
    private JTextField textFieldActiveStock;
    private JTextField textFieldAdditionStock;
    private JTextField textFieldSearch;
    private JButton buttonSearch;
    private final DialogHelper m_dialogHelper;
    private final ApplicationService m_applicationService;

    @PostConstruct
    private void postConstruct()
    {
        setSize(390, 290);
        setLocationRelativeTo(null);
        setTitle("Hızlı Stok Ekle");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(buttonOK);
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        pack();
        registerKeys();
        initializeButtons();
        dispose();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }
    private void registerKeys()
    {
        contentPane.registerKeyboardAction(this::onCancel,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(this::onOK,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    private void initializeButtons()
    {
        buttonOK.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
    }

    private void onOK(ActionEvent event)
    {
        dispose();
    }

    private void onCancel(ActionEvent event)
    {
        dispose();
    }
}
