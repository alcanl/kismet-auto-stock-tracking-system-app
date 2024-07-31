package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.LocalDate;
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
    private final ApplicationService m_applicationService;
    private final JFileChooser m_fileChooser;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private static final String ms_title = "Yeni Ürün Ekle";

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
        setIconImage(m_applicationContext.getBean("bean.image.icon.dialog.add.new.product",
                ImageIcon.class).getImage());
        pack();
        initializeButtons();
        registerKeys();
        initializeTextArea();
        dispose();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
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

            if (m_dialogHelper.areFieldsValid(productName, stockAmount, productShelfCode,
                    stockCode, productOriginalCode, stockThreshold)) {

                var stockDTO = new StockDTO();
                var stockAmountInt = Integer.parseInt(stockAmount);

                if (stockAmountInt < 0) {
                    m_dialogHelper.showUnSupportedFormatMessage("Stok Miktarı '0' dan küçük olamaz!");
                    dispose();
                    return;
                }
                stockDTO.setAmount(stockAmountInt);
                stockDTO.setThreshold(Integer.parseInt(stockThreshold));
                stockDTO.setShelfNumber(productShelfCode);

                var productDTO = new ProductDTO( productOriginalCode, stockCode, LocalDate.now(), productName,
                        m_imageFile, null, description);

                var newStockMovement = m_applicationService.saveNewStockMovement(new StockMovementDTO(), stockDTO, productDTO);
                m_dialogHelper.showProductSaveSuccess(newStockMovement);
                m_dialogHelper.notifyTables();
                dispose();
            }

        }
        catch (NumberFormatException ex)
        {
            m_dialogHelper.showUnSupportedFormatMessage(textFieldProductName.getText());
            log.error("DailogAddNewProduct::onOk : {} ",ex.getMessage());
        }
        catch (ExecutionException | InterruptedException ex)
        {
            if (ex.getCause() instanceof ProductAlreadyExistException)
                m_dialogHelper.showProductAlreadyExistMessage(textFieldProductOriginalCode.getText());
            else
                m_dialogHelper.showUnknownErrorMessage();

            log.error("DailogAddNewProduct::onOk : {} ",ex.getMessage());
        }
        catch (ServiceException ex) {
            m_dialogHelper.showUnknownErrorMessage();
            log.error("DailogAddNewProduct::onOk : {} ",ex.getMessage());
        }
    }

    private void onCancel(ActionEvent e)
    {
        dispose();
    }
}
