package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.service.ApplicationService;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("ALL")
public class DialogUpdateMaterial extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JPanel panelMain;
    private JPanel panelProps;
    private JPanel panelBottom;
    private JPanel panelButtons;
    private JPanel panelLabels;
    private JPanel panelTextFields;
    private JTextField textFieldUnitSalePrice;
    private JTextField textFieldUnitPrice;
    private JTextField textFieldRadius;
    private JTextField textFieldLength;
    private JTextField textFieldName;
    private JLabel labelUnitSalePrice;
    private JLabel labelName;
    private JLabel labelLength;
    private JLabel labelRadius;
    private JLabel labelUnitPrice;
    private JCheckBox checkBoxRatio;
    private JTextField textFieldRatio;
    private final ApplicationService m_applicationService;

    public DialogUpdateMaterial(ApplicationService applicationService) {
        m_applicationService = applicationService;
        setSize(390, 290);
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(buttonSave);

        buttonSave.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        checkBoxRatio.addActionListener(e -> checkBoxChanged());
        initializeTexFields();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void checkBoxChanged() {
        if (checkBoxRatio.isSelected()) {
            textFieldRatio.setEnabled(true);
            textFieldUnitPrice.setEnabled(false);
            textFieldName.setEnabled(false);
            textFieldLength.setEnabled(false);
            textFieldRadius.setEnabled(false);
        } else {
            textFieldRatio.setEnabled(false);
            textFieldUnitPrice.setEnabled(true);
            textFieldLength.setEnabled(true);
            textFieldName.setEnabled(true);
            textFieldRadius.setEnabled(true);
        }
    }

    private void initializeTexFields() {

    }

    private void onOK() {
    }

    private void onCancel() {
        dispose();
    }

}
