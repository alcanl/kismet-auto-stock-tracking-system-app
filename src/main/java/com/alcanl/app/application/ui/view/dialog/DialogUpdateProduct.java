package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.service.ApplicationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

@Slf4j
@SwingContainer
@Component("bean.dialog.update.product")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
@SuppressWarnings("ALL")
public class DialogUpdateProduct extends JDialog {
    private JPanel contentPaneMain;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JPanel mainPanel;
    private JPanel panelDialog;
    private JTextField textFieldProductName;
    private JTextField textFieldStockAmount;
    private JTextField textFieldProductShelfCode;
    private JButton buttonAddFile;
    private JLabel labelStockCode;
    private JLabel labelStockAmount;
    private JLabel labelShelfCode;
    private JLabel labelProductImage;
    private JLabel labelProductOriginalCode;
    private JLabel labelProductName;
    private JLabel labelThreshold;
    private JLabel labelDescription;
    private JTextField textFieldStockCode;
    private JTextField textFieldProductOriginalCode;
    private JTextField textFieldThreshold;
    private JTextArea textFieldDescription;
    private JPanel panelSaveProcess;
    private File m_imageFile;
    private final JFileChooser m_fileChooser;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final DialogHelper m_dialogHelper;
    private static final String ms_title = "Ürün Bilgisi Düzenle";

    @PostConstruct
    private void postConstruct()
    {
        setTitle(ms_title);
        setContentPane(contentPaneMain);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(buttonSave);
        setLocationRelativeTo(null);
        setIconImage(m_applicationContext.getBean("bean.image.icon.dialog.edit.product",
                ImageIcon.class).getImage());
        pack();
        setLocationRelativeTo(null);
        initializeButtons();
        initializeTexFields();
        registerKeys();
        dispose();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    private void registerKeys()
    {
        contentPaneMain.registerKeyboardAction(ignored -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPaneMain.registerKeyboardAction(this::onOK,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initializeButtons()
    {
        buttonAddFile.addActionListener(this::onAddFileClicked);
        buttonSave.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
    }
    private void onAddFileClicked(ActionEvent ignored)
    {
        var returnVal = m_fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            m_imageFile = m_fileChooser.getSelectedFile();
            buttonAddFile.setIcon((Icon) m_applicationContext.getBean("bean.image.icon.info.success.tick"));
            log.info(m_imageFile.getAbsolutePath());
        }
    }
    private void initializeTexFields()
    {
        var productDTO = m_dialogHelper.getSelectedProduct();
        textFieldProductOriginalCode.setText(productDTO.getOriginalCode());
        textFieldProductName.setText(productDTO.getProductName());
        textFieldStockCode.setText(productDTO.getStockCode());
        textFieldProductShelfCode.setText(productDTO.getStock().getShelfNumber());
        textFieldStockAmount.setText("%d".formatted(productDTO.getStock().getAmount()));
        textFieldThreshold.setText("%d".formatted(productDTO.getStock().getThreshold()));
        textFieldDescription.setText(productDTO.getDescription());

        if (productDTO.getImageFile() != null)
            buttonAddFile.setIcon(m_applicationContext.getBean("bean.image.icon.info.success.tick",
                    ImageIcon.class));
    }
    private void onOK(ActionEvent ignored)
    {
        var productName = textFieldProductName.getText();
        var stockAmount = textFieldStockAmount.getText();
        var productShelfCode = textFieldProductShelfCode.getText();
        var stockCode = textFieldStockCode.getText();
        var productOriginalCode = textFieldProductOriginalCode.getText();
        var stockThreshold = textFieldThreshold.getText();
        var description = textFieldDescription.getText();

        if (m_dialogHelper.areFieldsValid(productName, stockAmount, productShelfCode,
                stockCode, productOriginalCode, stockThreshold)) {
            //
        }
    }
    private void onCancel(ActionEvent ignored)
    {
        dispose();
    }

}
