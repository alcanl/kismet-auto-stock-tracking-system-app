package com.alcanl.app.application.ui.view.dialog;

import lombok.Getter;
import javax.swing.*;

public class DialogProductCard extends JDialog {
    @Getter
    private JPanel contentPaneMain;
    private JButton buttonOK;

    public DialogProductCard() {
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }

    private void onOK()
    {
        dispose();
    }
}
