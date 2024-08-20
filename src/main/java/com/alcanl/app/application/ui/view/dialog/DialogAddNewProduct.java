package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
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
    private void postConstruct() {
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

    private void initializeButtons() {
        buttonSave.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
        buttonAddFile.addActionListener(this::getSelectedFileCallback);
    }

    private void registerKeys() {
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
        } catch (NumberFormatException ex) {
            m_dialogHelper.showUnSupportedFormatMessage(textFieldProductName.getText());
            log.error("DialogAddNewProduct::onOk : NumberFormatEx : {} ", ex.getMessage());
        } catch (ExecutionException | InterruptedException ex) {
            if (ex.getCause() instanceof ProductAlreadyExistException)
                m_dialogHelper.showProductAlreadyExistMessage(textFieldProductOriginalCode.getText());
            else
                m_dialogHelper.showUnknownErrorMessageWhileSavingProduct();

            log.error("DialogAddNewProduct::onOk : Execution,InterruptedEx : {} ", ex.getMessage());
        } catch (ServiceException ex) {
            m_dialogHelper.showUnknownErrorMessageWhileSavingProduct();
            log.error("DialogAddNewProduct::onOk : ServiceEx : {} ", ex.getMessage());
        }
    }

    private void onCancel(ActionEvent e) {
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPaneMain = new JPanel();
        contentPaneMain.setLayout(new GridLayoutManager(1, 1, new Insets(15, 40, 30, 40), -1, -1));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(20, 30, 20, 30), -1, -1));
        contentPaneMain.add(mainPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDialog = new JPanel();
        panelDialog.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panelDialog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldProductName = new JTextField();
        panel1.add(textFieldProductName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldStockAmount = new JTextField();
        panel2.add(textFieldStockAmount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel3, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldProductShelfCode = new JTextField();
        panel3.add(textFieldProductShelfCode, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel4, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonAddFile = new JButton();
        buttonAddFile.setIcon(new ImageIcon(getClass().getResource("/icons/icon_add.png")));
        buttonAddFile.setText("Görsel Ekle");
        panel4.add(buttonAddFile, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel5, new GridConstraints(0, 0, 8, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelStockCode = new JLabel();
        Font labelStockCodeFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelStockCode.getFont());
        if (labelStockCodeFont != null) labelStockCode.setFont(labelStockCodeFont);
        labelStockCode.setForeground(new Color(-12169629));
        labelStockCode.setText("Ürün Stok Kodu : ");
        panel6.add(labelStockCode, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelStockAmount = new JLabel();
        Font labelStockAmountFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelStockAmount.getFont());
        if (labelStockAmountFont != null) labelStockAmount.setFont(labelStockAmountFont);
        labelStockAmount.setForeground(new Color(-12169629));
        labelStockAmount.setText("Ürün Stok Miktarı : ");
        panel7.add(labelStockAmount, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel8, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelShelfCode = new JLabel();
        Font labelShelfCodeFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelShelfCode.getFont());
        if (labelShelfCodeFont != null) labelShelfCode.setFont(labelShelfCodeFont);
        labelShelfCode.setForeground(new Color(-12169629));
        labelShelfCode.setText("Ürün Raf Kodu : ");
        panel8.add(labelShelfCode, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel9, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelProductImage = new JLabel();
        Font labelProductImageFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelProductImage.getFont());
        if (labelProductImageFont != null) labelProductImage.setFont(labelProductImageFont);
        labelProductImage.setForeground(new Color(-12169629));
        labelProductImage.setText("Ürün Görseli : ");
        panel9.add(labelProductImage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelProductOriginalCode = new JLabel();
        Font labelProductOriginalCodeFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelProductOriginalCode.getFont());
        if (labelProductOriginalCodeFont != null) labelProductOriginalCode.setFont(labelProductOriginalCodeFont);
        labelProductOriginalCode.setForeground(new Color(-12169629));
        labelProductOriginalCode.setText("Ürün Orijinal Kodu :");
        panel10.add(labelProductOriginalCode, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelProductName = new JLabel();
        Font labelProductNameFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelProductName.getFont());
        if (labelProductNameFont != null) labelProductName.setFont(labelProductNameFont);
        labelProductName.setForeground(new Color(-12169629));
        labelProductName.setText("Ürün Adı :");
        panel11.add(labelProductName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), 0, 0));
        panel5.add(panel12, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelThreshold = new JLabel();
        Font labelThresholdFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelThreshold.getFont());
        if (labelThresholdFont != null) labelThreshold.setFont(labelThresholdFont);
        labelThreshold.setForeground(new Color(-12169629));
        labelThreshold.setText("Stok Eşik Değeri :");
        panel12.add(labelThreshold, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel13, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 50), null, new Dimension(-1, 50), 0, false));
        labelDescription = new JLabel();
        Font labelDescriptionFont = this.$$$getFont$$$("Calibri", Font.PLAIN, 14, labelDescription.getFont());
        if (labelDescriptionFont != null) labelDescription.setFont(labelDescriptionFont);
        labelDescription.setForeground(new Color(-12169629));
        labelDescription.setText("Açıklama :");
        panel13.add(labelDescription, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel14, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldStockCode = new JTextField();
        panel14.add(textFieldStockCode, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel15, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldProductOriginalCode = new JTextField();
        panel15.add(textFieldProductOriginalCode, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel16, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldThreshold = new JTextField();
        panel16.add(textFieldThreshold, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelDialog.add(panel17, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 50), null, new Dimension(-1, 50), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel17.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textFieldDescription = new JTextArea();
        scrollPane1.setViewportView(textFieldDescription);
        panelSaveProcess = new JPanel();
        panelSaveProcess.setLayout(new GridLayoutManager(1, 3, new Insets(20, 0, 0, 0), -1, -1));
        mainPanel.add(panelSaveProcess, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelSaveProcess.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Kaydet");
        panelSaveProcess.add(buttonSave, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panelSaveProcess.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPaneMain;
    }
}
