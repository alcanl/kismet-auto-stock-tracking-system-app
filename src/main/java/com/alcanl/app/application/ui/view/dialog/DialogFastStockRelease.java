package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.service.dto.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

@SwingContainer
@Slf4j
@Component("bean.dialog.fast.stock.release")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
public class DialogFastStockRelease extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<ProductDTO> listProductName;
    private JTextField textFieldActiveStock;
    private JTextField textFieldReleaseStock;
    private JTextField textFieldSearch;
    private JButton buttonSearch;
    private final DefaultListModel<ProductDTO> m_listModel;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private final Vector<ProductDTO> m_listData = new Vector<>();
    private static final String ms_title = "Hızlı Stok Çık";

    @PostConstruct
    private void postConstruct()
    {
        m_dialogHelper.initializeDialog(this, contentPane, ms_title, buttonOK,
                m_applicationContext.getBean("bean.image.icon.dialog.release.stock",
                        ImageIcon.class));
        registerKeys();
        initializeButtons();
        initializeJList();
        buttonSearch.addActionListener(this::onSearchButtonClickedCallback);
    }

    public void initializeTextFields()
    {
        textFieldSearch.setEnabled(false);
        textFieldSearch.setEditable(false);
        buttonSearch.setEnabled(false);
        m_listData.add(m_dialogHelper.getSelectedProduct());
        listProductName.setListData(m_listData);
        listProductName.updateUI();
        listProductName.setSelectedIndex(0);
    }
    private void initializeJList()
    {
        listProductName.setModel(m_listModel);
        listProductName.setListData(m_listData);
        listProductName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listProductName.addListSelectionListener(this::onListItemSelectedCallback);
    }
    private void onSearchButtonClickedCallback(ActionEvent e)
    {
        try {
            m_dialogHelper.addOrReleaseDialogOnSearchButtonClickedCallback(
                    m_listData, m_listModel, textFieldSearch.getText(), listProductName);

        } catch (ExecutionException | InterruptedException ex)
        {
            log.error("DialogFastStockRelease::onSearchButtonClicked : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessage();
        }

    }
    private void onListItemSelectedCallback(ListSelectionEvent ignore)
    {
        var productDTO = listProductName.getSelectedValue();
        textFieldActiveStock.setText("%d".formatted(productDTO.getStock().getAmount()));
        textFieldSearch.setText(productDTO.getProductName());
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

    private void onOK(ActionEvent ignored)
    {
        try {
            var stockAdditionAmountText = textFieldReleaseStock.getText();
            if (m_dialogHelper.areFieldsValid(stockAdditionAmountText)) {
                var stockReleaseAmount = Integer.parseInt(stockAdditionAmountText);
                var productDTO = listProductName.getSelectedValue();

                if (productDTO == null) {
                    m_dialogHelper.showNoSelectedProductMessage();
                    return;
                }

                if (stockReleaseAmount <= 0 || (productDTO.getStock().getAmount() - stockReleaseAmount) < 0) {
                    m_dialogHelper.showUnSupportedFormatMessage("Stok Miktarı '0' 'dan Küçük Olamaz!");
                    return;
                }

                productDTO.getStock().setAmount(productDTO.getStock().getAmount() - stockReleaseAmount);
                m_dialogHelper.showProductSaveSuccess(m_dialogHelper.saveNewStockMovement(productDTO,
                        stockReleaseAmount, StockMovementType.STOCK_OUTPUT));
                m_dialogHelper.notifyTables();
                dispose();
            }
        }
        catch (NumberFormatException ex)
        {
            log.error("DialogFastStockRelease::onOK : {}", ex.getMessage());
            m_dialogHelper.showUnSupportedFormatMessage(textFieldReleaseStock.getText());

        } catch (ExecutionException | InterruptedException ex)
        {
            log.error("DialogFastStockRelease::onOk : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessage();
        }
    }

    private void onCancel(ActionEvent event)
    {
        dispose();
    }
}
