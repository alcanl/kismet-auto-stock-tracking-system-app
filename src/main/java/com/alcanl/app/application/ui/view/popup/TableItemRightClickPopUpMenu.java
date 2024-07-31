package com.alcanl.app.application.ui.view.popup;

import com.alcanl.app.helper.PopUpHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.alcanl.app.helper.PopUpHelper.*;

@Component("bean.table.popup.click.right")
@Scope("prototype")
public class TableItemRightClickPopUpMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem addStockSelectedRow;
    private final JMenuItem deleteSelectedRow;
    private final JMenuItem getProductCard;
    private final PopUpHelper m_popUpHelper;

    public TableItemRightClickPopUpMenu(PopUpHelper popUpHelper)
    {
        m_popUpHelper = popUpHelper;
        addStockSelectedRow = new JMenuItem(FAST_STOCK_ADD_TEXT);
        deleteSelectedRow = new JMenuItem(DELETE_PRODUCT_TEXT);
        getProductCard = new JMenuItem(GET_PRODUCT_CART_TEXT);
        pack();
        initializeMenu();
    }

    private void initializeMenu()
    {
        getProductCard.addActionListener(this);
        addStockSelectedRow.addActionListener(this);
        deleteSelectedRow.addActionListener(this);

        add(addStockSelectedRow);
        add(getProductCard);
        addSeparator();
        add(deleteSelectedRow);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(getProductCard))
            m_popUpHelper.newProductCardWithProduct();
        else if (e.getSource().equals(deleteSelectedRow))
            m_popUpHelper.deleteSelectedProduct();
        else
            m_popUpHelper.newStockInputWithProduct();

    }
}