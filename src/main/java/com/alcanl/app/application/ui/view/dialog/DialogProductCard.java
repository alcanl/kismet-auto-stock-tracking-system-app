package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.google.common.io.Files;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@SwingContainer
@Component("bean.dialog.card.product")
@Scope("prototype")
@Lazy
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
    private JTextField textFieldProductRegisterDate;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private final DateTimeFormatter m_dateTimeFormatter;
    private static final String ms_title = "Ürün Kartı";

    @PostConstruct
    private void postConstruct()
    {
        setLocationRelativeTo(null);
        setTitle(ms_title);
        setContentPane(contentPaneMain);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setIconImage(m_applicationContext.getBean("bean.image.icon.dialog.card.product",
                ImageIcon.class).getImage());
        pack();
        setLocationRelativeTo(null);
        initializeButtons();
        registerKeys();
        dispose();

        try {
            initializeTextViews();
        } catch (IOException ignore) {}

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void registerKeys()
    {
        contentPaneMain.registerKeyboardAction(ignored -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPaneMain.registerKeyboardAction(this::onOK,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initializeButtons()
    {
        buttonOK.addActionListener(this::onOK);
        buttonPrintCart.addActionListener(this::onPrintCart);
    }

    private void initializeTextViews() throws IOException
    {
        if (m_dialogHelper.getSelectedProduct() == null) {
            dispose();
            return;
        }

        textFieldDescription.setLineWrap(true);
        textFieldDescription.setWrapStyleWord(true);
        textFieldProductName.setText(m_dialogHelper.getSelectedProduct().getProductName());
        textFieldProductOriginalCode.setText(m_dialogHelper.getSelectedProduct().getOriginalCode());
        textFieldStockCode.setText(m_dialogHelper.getSelectedProduct().getStockCode());
        textFieldStockAmount.setText("%d".formatted(m_dialogHelper.getSelectedProduct().getStock().getAmount()));
        textFieldStockThreshold.setText("%d".formatted(m_dialogHelper.getSelectedProduct().getStock().getThreshold()));
        textFieldDescription.setText(m_dialogHelper.getSelectedProduct().getDescription());
        textFieldShelfCode.setText(m_dialogHelper.getSelectedProduct().getStock().getShelfNumber());
        textFieldProductRegisterDate.setText(m_dateTimeFormatter.format(m_dialogHelper.getSelectedProduct().getRegisterDate()));
        textFieldProductName.setEditable(false);
        textFieldProductOriginalCode.setEditable(false);
        textFieldStockCode.setEditable(false);
        textFieldStockAmount.setEditable(false);
        textFieldStockThreshold.setEditable(false);
        textFieldDescription.setEditable(false);
        textFieldShelfCode.setEditable(false);
        textFieldProductRegisterDate.setEditable(false);
        var imageFile = m_dialogHelper.getSelectedProduct().getImageFile();
        if (imageFile != null) {
            var image = new ImageIcon(Files.toByteArray(imageFile)).getImage();
            var imageScale = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            var scaledIcon = new ImageIcon(imageScale);
            labelProductImage.setIcon(scaledIcon);
        }
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
