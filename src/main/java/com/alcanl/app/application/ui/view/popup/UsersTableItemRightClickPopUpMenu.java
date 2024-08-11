package com.alcanl.app.application.ui.view.popup;

import com.alcanl.app.helper.popup.PopUpHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.alcanl.app.helper.popup.PopUpHelper.*;

@Component("bean.table.users.popup.click.right")
@Scope("prototype")
public class UsersTableItemRightClickPopUpMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem editSelectedRow;
    private final JMenuItem deleteSelectedRow;
    private final PopUpHelper m_popUpHelper;

    public UsersTableItemRightClickPopUpMenu(PopUpHelper popUpHelper)
    {
        m_popUpHelper = popUpHelper;
        editSelectedRow = new JMenuItem(EDIT_USER_TEXT);
        deleteSelectedRow = new JMenuItem(DELETE_USER_TEXT);
        initializeMenu();
        pack();
    }

    private void initializeMenu()
    {
        editSelectedRow.addActionListener(this);
        deleteSelectedRow.addActionListener(this);
        add(editSelectedRow);
        add(deleteSelectedRow);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == editSelectedRow)
            m_popUpHelper.editSelectedUser();
        else
            m_popUpHelper.deleteSelectedUser();
    }
}
