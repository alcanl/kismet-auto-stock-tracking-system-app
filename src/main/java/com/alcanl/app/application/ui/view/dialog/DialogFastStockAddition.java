package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
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
    private final DefaultListModel<ProductDTO> m_listModel;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final Vector<ProductDTO> m_listData = new Vector<>();
    private static final String ms_title = "Hızlı Stok Ekle";

    @PostConstruct
    private void postConstruct()
    {
        setLocationRelativeTo(null);
        setTitle(ms_title);
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(buttonOK);
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setIconImage(m_applicationContext.getBean("bean.image.icon.dialog.add.stock",
                ImageIcon.class).getImage());
        pack();
        setLocationRelativeTo(null);
        registerKeys();
        initializeButtons();
        initializeJList();
        buttonSearch.addActionListener(this::onSearchButtonClickedCallback);
        dispose();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

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
            m_listData.clear();
            m_listModel.clear();
            m_applicationService.findAllProductsByContains(textFieldSearch.getText())
                    .forEach(m_listModel::addElement);
            listProductName.setModel(m_listModel);
            listProductName.updateUI();

        } catch (ExecutionException | InterruptedException ex)
        {
            log.error("DialogFastStockAddition::onSearchButtonClicked : {}", ex.getMessage());
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

    private void onOK(ActionEvent event)
    {
        try {
            var stockAdditionAmountText = textFieldAdditionStock.getText();
            if (m_dialogHelper.areFieldsValid(stockAdditionAmountText)) {
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

                var stockMovementDTO = new StockMovementDTO();
                stockMovementDTO.setStock(productDTO.getStock());
                stockMovementDTO.setStockMovementType(StockMovementType.STOCK_INPUT);
                stockMovementDTO.setAmount(stockAdditionAmount);

                var newStockMovement = m_applicationService.saveNewStockMovementWithUpdateItem(stockMovementDTO, productDTO);
                m_dialogHelper.showProductSaveSuccess(newStockMovement);
                m_dialogHelper.notifyTables();
                dispose();
            }
        }
        catch (NumberFormatException ex)
        {
            log.error("DialogFastStockAddition::onOK : {}", ex.getMessage());
            m_dialogHelper.showUnSupportedFormatMessage(textFieldAdditionStock.getText());

        } catch (ExecutionException | InterruptedException ex)
        {
            log.error("DialogFastStockAddition::onOk : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessage();
        }
    }

    private void onCancel(ActionEvent event)
    {
        dispose();
    }
}
