package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationContext;
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
@Component("bean.dialog.add.new.product")
@Scope("prototype")
@Slf4j
public class DialogAddNewProduct extends JDialog {

    private JPanel contentPane;
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
    private File m_imageFile = null;
    private final ApplicationService m_applicationService;
    private final JFileChooser m_fileChooser;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;

    public DialogAddNewProduct(ApplicationService applicationService, JFileChooser fileFilter,
                               DialogHelper dialogHelper, ApplicationContext applicationContext) {

        m_applicationService = applicationService;
        m_fileChooser = fileFilter;
        m_dialogHelper = dialogHelper;
        m_applicationContext = applicationContext;
    }
    @PostConstruct
    private void postConstruct()
    {
        setSize(390, 290);
        setContentPane(contentPane);
        setTitle("Yeni Ürün Ekle");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(buttonSave);
        buttonSave.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
        buttonAddFile.addActionListener(this::getSelectedFileCallback);
        dispose();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(e),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
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

            if (m_dialogHelper.areFieldsValid(productName, stockAmount, productShelfCode,
                    stockCode, productOriginalCode, stockThreshold)) {

                var stockDTO = new StockDTO();
                stockDTO.setAmount(Integer.parseInt(stockAmount));
                stockDTO.setThreshold(Integer.parseInt(stockThreshold));
                stockDTO.setShelfNumber(productShelfCode);

                var productDTO = new ProductDTO(productOriginalCode, stockCode, productName,
                        m_imageFile, null);
                m_applicationService.saveProduct(productDTO, stockDTO);
                m_dialogHelper.showProductSaveSuccess();
                m_dialogHelper.notifyTables();
                dispose();
            }

        } catch (NumberFormatException ex) {
            m_dialogHelper.showUnSupportedFormatMessage(textFieldProductName.getText());
        } catch (ProductAlreadyExistException ex) {
            m_dialogHelper.showProductAlreadyExistMessage(ex.getMessage());
        } catch (ServiceException | ExecutionException | InterruptedException ex) {
            m_dialogHelper.showUnknownErrorMessage();
        }
    }

    private void onCancel(ActionEvent e)
    {
        dispose();
    }
}
