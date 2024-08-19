package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.service.dto.ProductDTO;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

@SwingContainer
@Slf4j
@Component("bean.dialog.fast.stock.addition")
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
    private final DefaultListModel<ProductDTO> m_listModel;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private final Vector<ProductDTO> m_listData = new Vector<>();
    private static final String ms_title = "Hızlı Stok Ekle";

    @PostConstruct
    private void postConstruct() {
        m_dialogHelper.initializeDialog(this, contentPane, ms_title, buttonSearch,
                m_applicationContext.getBean("bean.image.icon.dialog.add.stock",
                        ImageIcon.class));
        registerKeys();
        initializeButtons();
        initializeJList();
        buttonSearch.addActionListener(this::onSearchButtonClickedCallback);
    }

    public void initializeTextFields() {
        textFieldSearch.setEnabled(false);
        textFieldSearch.setEditable(false);
        buttonSearch.setEnabled(false);
        m_listData.add(m_dialogHelper.getSelectedProduct());
        listProductName.setListData(m_listData);
        listProductName.updateUI();
        listProductName.setSelectedIndex(0);
    }

    private void initializeJList() {
        listProductName.setModel(m_listModel);
        listProductName.setListData(m_listData);
        listProductName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listProductName.addListSelectionListener(this::onListItemSelectedCallback);
    }

    private void onSearchButtonClickedCallback(ActionEvent e) {
        try {
            m_dialogHelper.addOrReleaseDialogOnSearchButtonClickedCallback(
                    m_listData, m_listModel, textFieldSearch.getText(), listProductName);

        } catch (ExecutionException | InterruptedException ex) {
            log.error("DialogFastStockAddition::onSearchButtonClicked : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageWhileSavingProduct();
        }

    }

    private void onListItemSelectedCallback(ListSelectionEvent ignore) {
        var productDTO = listProductName.getSelectedValue();
        textFieldActiveStock.setText("%d".formatted(productDTO.getStock().getAmount()));
        textFieldSearch.setText(productDTO.getProductName());
    }

    private void registerKeys() {
        contentPane.registerKeyboardAction(this::onCancel,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(this::onOK,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initializeButtons() {
        buttonOK.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
    }

    private void onOK(ActionEvent event) {
        try {
            var stockAdditionAmountText = textFieldAdditionStock.getText();
            if (m_dialogHelper.areProductFieldsValid(stockAdditionAmountText)) {
                var stockAdditionAmount = Integer.parseInt(stockAdditionAmountText);
                if (stockAdditionAmount <= 0) {
                    m_dialogHelper.showUnSupportedFormatMessage("Stok Miktarı '0' 'dan Küçük Olamaz!");
                    return;
                }
                var productDTO = listProductName.getSelectedValue();

                if (productDTO == null) {
                    m_dialogHelper.showNoSelectedProductMessage();
                    return;
                }

                productDTO.getStock().setAmount(productDTO.getStock().getAmount() + stockAdditionAmount);
                m_dialogHelper.showProductSaveSuccess(m_dialogHelper.saveNewStockMovement(productDTO,
                                stockAdditionAmount, StockMovementType.STOCK_INPUT).getStock()
                        .getProduct().getOriginalCode());
                m_dialogHelper.notifyTables();
                dispose();
            }
        } catch (NumberFormatException ex) {
            log.error("DialogFastStockAddition::onOK : {}", ex.getMessage());
            m_dialogHelper.showUnSupportedFormatMessage(textFieldAdditionStock.getText());

        } catch (ExecutionException | InterruptedException ex) {
            log.error("DialogFastStockAddition::onOk : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessageWhileSavingProduct();
        }
    }

    private void onCancel(ActionEvent ignored) {
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(15, 40, 30, 40), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 20, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Kaydet");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("İptal");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 1, new Insets(20, 5, 20, 5), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listProductName = new JList();
        listProductName.setSelectionMode(0);
        scrollPane1.setViewportView(listProductName);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Calibri", Font.BOLD, 16, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-12169629));
        label1.setText("Aranacak Ürün Adı");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldSearch = new JTextField();
        textFieldSearch.setHorizontalAlignment(0);
        panel5.add(textFieldSearch, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonSearch = new JButton();
        buttonSearch.setHorizontalTextPosition(10);
        buttonSearch.setIcon(new ImageIcon(getClass().getResource("/icons/icon_search.png")));
        buttonSearch.setText("Ara");
        panel5.add(buttonSearch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Calibri", Font.BOLD, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-12169629));
        label2.setText("Ürün Mevcut Stok");
        panel6.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldActiveStock = new JTextField();
        textFieldActiveStock.setEditable(false);
        textFieldActiveStock.setEnabled(false);
        textFieldActiveStock.setHorizontalAlignment(0);
        panel6.add(textFieldActiveStock, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Calibri", Font.BOLD, 16, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-12169629));
        label3.setText("Eklenilecek Stok Miktarı");
        panel6.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldAdditionStock = new JTextField();
        textFieldAdditionStock.setHorizontalAlignment(0);
        panel6.add(textFieldAdditionStock, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel6.add(spacer3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel6.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
        return contentPane;
    }
}
