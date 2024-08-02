package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.repository.entity.type.UpdateOperationType;
import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import com.alcanl.app.service.dto.StockDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("ALL")
@Slf4j
@SwingContainer
@Component("bean.dialog.edit.product")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
public class DialogEditProduct extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
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
    private JButton buttonSave;
    private JComboBox<UpdateOperationType> comboBoxUpdateType;
    private File m_imageFile;
    private UpdateOperationType m_updateOperationType;
    private final JFileChooser m_fileChooser;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private static final String ms_title = "Ürün Bilgilerini Düzenle";

    @PostConstruct
    private void postConstruct()
    {
        m_dialogHelper.initializeDialog(this, contentPane, ms_title, buttonSave,
                m_applicationContext.getBean("bean.image.icon.dialog.edit.product", ImageIcon.class));
        initializeButtons();
        registerKeys();
        initializeTextArea();
        fillTextFields();
        initializeComboBox();
        dispose();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    private void initializeButtons()
    {
        buttonSave.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
        buttonAddFile.addActionListener(this::getSelectedFileCallback);
    }
    private void registerKeys()
    {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(e),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(e -> onOK(e),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    private void fillTextFields()
    {
        var productDTO = m_dialogHelper.getSelectedProduct();
        textFieldProductName.setText(productDTO.getProductName());
        textFieldDescription.setText(productDTO.getDescription());
        textFieldProductOriginalCode.setText(productDTO.getOriginalCode());
        textFieldStockCode.setText(productDTO.getStockCode());
        textFieldStockAmount.setText("%d".formatted(productDTO.getStock().getAmount()));
        textFieldThreshold.setText("%d".formatted(productDTO.getStock().getThreshold()));
        textFieldProductShelfCode.setText(productDTO.getStock().getShelfNumber());

        if (productDTO.getImageFile() != null)
            buttonAddFile.setIcon(m_applicationContext.getBean("bean.image.icon.info.success.tick", ImageIcon.class));
    }
    private void initializeTextArea()
    {
        textFieldDescription.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        textFieldDescription.setFocusTraversalKeys (KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));

        textFieldDescription.setLineWrap(true);
        textFieldDescription.setWrapStyleWord(true);
        textFieldDescription.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null || textFieldDescription.getText().length() >= 255)
                    return;

                super.insertString(offs, str, a);
            }
        });
    }

    private void initializeComboBox()
    {
        comboBoxUpdateType.addItem(UpdateOperationType.PRODUCT_UPDATE);
        comboBoxUpdateType.addItem(UpdateOperationType.STOCK_UPDATE);
        comboBoxUpdateType.addItem(UpdateOperationType.BOTH);
        comboBoxUpdateType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                m_updateOperationType = (UpdateOperationType) e.getItem();
            }
        });
    }

    private void getSelectedFileCallback(ActionEvent e)
    {
        var returnVal = m_fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            m_imageFile = m_fileChooser.getSelectedFile();
            buttonAddFile.setIcon((Icon) m_applicationContext.getBean("bean.image.icon.info.success.tick"));
            log.info(m_imageFile.getAbsolutePath());
        }
    }

    private void onOK(ActionEvent ignored) {
        try {
            var productName = textFieldProductName.getText();
            var stockAmount = textFieldStockAmount.getText();
            var productShelfCode = textFieldProductShelfCode.getText();
            var stockCode = textFieldStockCode.getText();
            var productOriginalCode = textFieldProductOriginalCode.getText();
            var stockThreshold = textFieldThreshold.getText();
            var description = textFieldDescription.getText();

            if (m_dialogHelper.areFieldsValid(productName, stockAmount, productShelfCode,
                    stockCode, productOriginalCode, stockThreshold)) {

                var stockDTO = new StockDTO();
                var stockAmountInt = Integer.parseInt(stockAmount);
                var stockThresholdInt = Integer.parseInt(stockThreshold);

                if (stockAmountInt < 0 || stockThresholdInt < 0) {
                    m_dialogHelper.showUnSupportedFormatMessage("Stok Miktarı / Stok Eşik Miktarı '0' dan küçük olamaz!");
                    dispose();
                    return;
                }

                if (m_updateOperationType == null)
                    m_updateOperationType = UpdateOperationType.PRODUCT_UPDATE;

                m_dialogHelper.showProductSaveSuccess(
                        m_dialogHelper.saveNewUpdateOperation(stockAmountInt, stockThresholdInt, productShelfCode,
                                productOriginalCode, stockCode, productName, m_imageFile, description, m_updateOperationType)
                                .getStock().getProduct().getOriginalCode());
                m_dialogHelper.notifyTables();
                dispose();
            }
        }
        catch (NumberFormatException ex)
        {
            m_dialogHelper.showUnSupportedFormatMessage(textFieldProductName.getText());
            log.error("DailogEditProduct::onOk : {} ",ex.getMessage());
        }
        catch (ExecutionException | InterruptedException ex)
        {
            if (ex.getCause() instanceof ProductAlreadyExistException)
                m_dialogHelper.showProductAlreadyExistMessage(textFieldProductOriginalCode.getText());
            else
                m_dialogHelper.showUnknownErrorMessage();

            log.error("DailogEditProduct::onOk : {} ",ex.getMessage());
        }
        catch (ServiceException ex) {
            m_dialogHelper.showUnknownErrorMessage();
            log.error("DailogEditProduct::onOk : {} ",ex.getMessage());
        }
    }

    private void onCancel(ActionEvent ignored)
    {
        dispose();
    }
}
