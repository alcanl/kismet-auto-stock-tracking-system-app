package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
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
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
@Slf4j
@SwingContainer
@Component("bean.dialog.add.new.product")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
public class DialogAddNewProduct extends JDialog {

    @Getter
    private JPanel contentPaneMain;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JPanel panelSaveProcess;
    private JPanel panelDialog;
    private JTextField textFieldProductName;
    private JTextField textFieldStockAmount;
    private JTextField textFieldProductShelfCode;
    private JLabel labelStockCode;
    private JLabel labelStockAmount;
    private JLabel labelProductImage;
    private JPanel mainPanel;
    private JLabel labelShelfCode;
    private JLabel labelProductName;
    private JLabel labelProductOriginalCode;
    private JTextField textFieldStockCode;
    private JTextField textFieldProductOriginalCode;
    private JButton buttonAddFile;
    private JTextField textFieldThreshold;
    private JLabel labelThreshold;
    private JTextArea textFieldDescription;
    private JLabel labelDescription;
    private File m_imageFile;
    private final JFileChooser m_fileChooser;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private static final String ms_title = "Yeni Ürün Ekle";

    @PostConstruct
    private void postConstruct()
    {
        m_dialogHelper.initializeDialog(this, contentPaneMain, ms_title, buttonSave,
                m_applicationContext.getBean("bean.image.icon.dialog.add.new.product", ImageIcon.class));
        initializeButtons();
        registerKeys();
        m_dialogHelper.disableTextAreaGrowthBehaviour(textFieldDescription);
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
        contentPaneMain.registerKeyboardAction(e -> onCancel(e),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPaneMain.registerKeyboardAction(e -> onOK(e),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void getSelectedFileCallback(ActionEvent e) {
        var returnVal = m_fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            m_imageFile = m_fileChooser.getSelectedFile();
            buttonAddFile.setIcon((Icon) m_applicationContext.getBean("bean.image.icon.info.success.tick"));
            log.info(m_imageFile.getAbsolutePath());
        }
    }

    private void onOK(ActionEvent e) {
        try {
            var productName = textFieldProductName.getText();
            var stockAmount = textFieldStockAmount.getText();
            var productShelfCode = textFieldProductShelfCode.getText();
            var stockCode = textFieldStockCode.getText();
            var productOriginalCode = textFieldProductOriginalCode.getText();
            var stockThreshold = textFieldThreshold.getText();
            var description = textFieldDescription.getText();

            if (m_dialogHelper.areProductFieldsValid(productName, stockAmount, productShelfCode,
                    stockCode, productOriginalCode, stockThreshold)) {

                var stockAmountInt = Integer.parseInt(stockAmount);
                var stockThresholdInt = Integer.parseInt(stockThreshold);

                if (stockAmountInt < 0 || stockThresholdInt < 0) {
                    m_dialogHelper.showUnSupportedFormatMessage("Stok Miktarı / Stok Eşik Miktarı '0' dan küçük olamaz!");
                    dispose();
                    return;
                }

                m_dialogHelper.showProductSaveSuccess(
                        m_dialogHelper.saveNewStockMovementWithProductCreate(stockAmountInt, stockThresholdInt, productShelfCode, productOriginalCode,
                                stockCode, productName, m_imageFile, description).getStock().getProduct().getOriginalCode());
                m_dialogHelper.notifyTables();
                dispose();
            }
        }
        catch (NumberFormatException ex)
        {
            m_dialogHelper.showUnSupportedFormatMessage(textFieldProductName.getText());
            log.error("DialogAddNewProduct::onOk : NumberFormatEx : {} ",ex.getMessage());
        }
        catch (ExecutionException | InterruptedException ex)
        {
            if (ex.getCause() instanceof ProductAlreadyExistException)
                m_dialogHelper.showProductAlreadyExistMessage(textFieldProductOriginalCode.getText());
            else
                m_dialogHelper.showUnknownErrorMessageWhileSavingProduct();

            log.error("DialogAddNewProduct::onOk : Execution,InterruptedEx : {} ",ex.getMessage());
        }
        catch (ServiceException ex) {
            m_dialogHelper.showUnknownErrorMessageWhileSavingProduct();
            log.error("DialogAddNewProduct::onOk : ServiceEx : {} ",ex.getMessage());
        }
    }

    private void onCancel(ActionEvent e)
    {
        dispose();
    }
}
