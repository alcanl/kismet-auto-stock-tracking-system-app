package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

@SwingContainer
@Component("bean.dialog.product.search.for.edit")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
@Slf4j
public class DialogProductSearchForEdit extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<ProductDTO> listProducts;
    private JTextField textFieldProductName;
    private JButton buttonSearch;
    private final DialogHelper m_dialogHelper;
    private final ApplicationService m_applicationService;
    private final ApplicationContext m_applicationContext;
    private final Vector<ProductDTO> m_products = new Vector<>();
    private static final String ms_title = "Ürün Ara";

    @PostConstruct
    private void postConstruct()
    {
        m_dialogHelper.initializeDialog(this, contentPane, ms_title, buttonOK,
                m_applicationContext.getBean("bean.image.icon.dialog.search.product", ImageIcon.class));
        listProducts.setListData(m_products);
        initializeButtons();
        registerKeys();
        dispose();
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
        buttonSearch.addActionListener(this::buttonSearchOnClickedCallback);
    }
    private void buttonSearchOnClickedCallback(ActionEvent ignored) {

        try {
            listProducts.clearSelection();
            m_products.clear();
            m_products.addAll(m_applicationService.findAllProductsByContains(textFieldProductName.getText()));
            listProducts.setListData(m_products);
            listProducts.updateUI();
        }
        catch (ExecutionException | InterruptedException ex) {
            log.error("DialogProductSearchForEdit::buttonSearchOnClickedCallback : {}", ex.getMessage());
            m_dialogHelper.showUnknownErrorMessage();
        }
    }
    private void onOK(ActionEvent ignored)
    {
        var productDTO = listProducts.getSelectedValue();
        if (productDTO == null) {
            m_dialogHelper.showNoSelectedProductMessage();
            return;
        }
        dispose();
        m_dialogHelper.setSelectedProduct(productDTO);
        m_dialogHelper.showEditProductDialogWithProduct();
    }

    private void onCancel(ActionEvent ignored)
    {
        dispose();
    }
}
