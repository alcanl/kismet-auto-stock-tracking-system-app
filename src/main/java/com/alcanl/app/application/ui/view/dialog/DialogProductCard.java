package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.DialogHelper;
import com.google.common.io.Files;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

@SwingContainer
@Component("bean.dialog.card.product")
@Scope("prototype")
@RequiredArgsConstructor
public class DialogProductCard extends JDialog {
    @Getter
    private JPanel contentPaneMain;
    private JButton buttonOK;
    private JTextField textFieldProductName;
    private JTextField textFieldProductOriginalCode;
    private JTextField textFieldStockCode;
    private JTextField textFieldStockAmount;
    private JTextField textFieldStockThreshold;
    private JTextField textFieldShelfCode;
    private JButton buttonPrintCart;
    private JTextArea textFieldDescription;
    private JLabel labelProductImage;
    private final DialogHelper m_dialogHelper;

    @PostConstruct
    private void postConstruct()
    {
        setSize(390, 290);
        setLocationRelativeTo(null);
        setTitle("Ürün Kartı");
        setContentPane(contentPaneMain);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        buttonOK.addActionListener(this::onOK);
        buttonPrintCart.addActionListener(this::onPrintCart);
        contentPaneMain.registerKeyboardAction(ignored -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPaneMain.registerKeyboardAction(this::onOK,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        textFieldDescription.setLineWrap(true);
        textFieldDescription.setWrapStyleWord(true);
        try {
            initializeTextViews();
        } catch (IOException ignore) {}

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    private void initializeTextViews() throws IOException {
        textFieldProductName.setText(m_dialogHelper.getSelectedProduct().getProductName());
        textFieldProductOriginalCode.setText(m_dialogHelper.getSelectedProduct().getOriginalCode());
        textFieldStockCode.setText(m_dialogHelper.getSelectedProduct().getStockCode());
        textFieldStockAmount.setText("%d".formatted(m_dialogHelper.getSelectedProduct().getStock().getAmount()));
        textFieldStockThreshold.setText("%d".formatted(m_dialogHelper.getSelectedProduct().getStock().getThreshold()));
        textFieldDescription.setText(m_dialogHelper.getSelectedProduct().getDescription());
        textFieldShelfCode.setText(m_dialogHelper.getSelectedProduct().getStock().getShelfNumber());
        textFieldProductName.setEditable(false);
        textFieldProductOriginalCode.setEditable(false);
        textFieldStockCode.setEditable(false);
        textFieldStockAmount.setEditable(false);
        textFieldStockThreshold.setEditable(false);
        textFieldDescription.setEditable(false);
        textFieldShelfCode.setEditable(false);
        var imageFile = m_dialogHelper.getSelectedProduct().getImageFile();
        if (imageFile != null)
            labelProductImage.setIcon(new ImageIcon( Files.toByteArray(imageFile)));
    }

    private void onOK(ActionEvent e)
    {
        dispose();
    }
    private void onPrintCart(ActionEvent e)
    {
        m_dialogHelper.printLabel();
    }
}
