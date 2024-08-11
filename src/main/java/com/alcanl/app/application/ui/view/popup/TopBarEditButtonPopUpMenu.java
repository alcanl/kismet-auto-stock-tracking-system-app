package com.alcanl.app.application.ui.view.popup;

import com.alcanl.app.helper.popup.PopUpHelper;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.alcanl.app.helper.popup.PopUpHelper.EDIT_PRODUCT_TEXT;
import static com.alcanl.app.helper.popup.PopUpHelper.EDIT_USER_TEXT;

@Component("bean.menu.popup.top.bar.edit")
public class TopBarEditButtonPopUpMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem m_editProductMenuItem;
    private final JMenuItem m_editUserMenuItem;
    private final PopUpHelper m_popUpHelper;

    public TopBarEditButtonPopUpMenu(PopUpHelper popUpHelper)
    {
        m_popUpHelper = popUpHelper;
        m_editProductMenuItem = new JMenuItem(EDIT_PRODUCT_TEXT);
        m_editUserMenuItem = new JMenuItem(EDIT_USER_TEXT);
        initialize();
    }
    private void initialize()
    {
        m_editUserMenuItem.addActionListener(this);
        m_editProductMenuItem.addActionListener(this);
        add(m_editProductMenuItem);
        add(m_editUserMenuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == m_editProductMenuItem)
            m_popUpHelper.editProduct();
        else
            m_popUpHelper.editUser();
    }
}
