package com.alcanl.app.application.ui.view.popup;

import com.alcanl.app.helper.popup.PopUpHelper;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.alcanl.app.helper.popup.PopUpHelper.*;

@Component("bean.menu.popup.top.bar.new")
public class TopBarNewButtonPopUpMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem m_newProductMenuItem;
    private final JMenuItem m_newStockInputMenuItem;
    private final JMenuItem m_newStockOutputMenuItem;
    private final JMenuItem m_exitMenuItem;
    private final JMenuItem m_productCardMenuItem;
    private final PopUpHelper m_popUpHelper;

    public TopBarNewButtonPopUpMenu(PopUpHelper popUpHelper)
    {
        m_popUpHelper = popUpHelper;
        m_newProductMenuItem = new JMenuItem(NEW_PRODUCT_TEXT);
        m_exitMenuItem = new JMenuItem(EXIT_TEXT);
        m_productCardMenuItem = new JMenuItem(NEW_PRODUCT_CARD_TEXT);
        m_newStockInputMenuItem = new JMenuItem(NEW_STOCK_INPUT_TEXT);
        m_newStockOutputMenuItem = new JMenuItem(NEW_STOCK_OUTPUT_TEXT);
        initialize();
    }
    private void initialize()
    {
        m_newProductMenuItem.addActionListener(this);
        m_newStockInputMenuItem.addActionListener(this);
        m_newStockOutputMenuItem.addActionListener(this);
        m_exitMenuItem.addActionListener(this);
        m_productCardMenuItem.addActionListener(this);
        add(m_newProductMenuItem);
        add(m_newStockInputMenuItem);
        add(m_newStockOutputMenuItem);
        add(m_productCardMenuItem);
        addSeparator();
        add(m_exitMenuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(m_newProductMenuItem))
            m_popUpHelper.newProduct();
        else if (e.getSource().equals(m_newStockInputMenuItem))
            m_popUpHelper.newStockInput();
        else if (e.getSource().equals(m_newStockOutputMenuItem))
            m_popUpHelper.newStockOutput();
        else if (e.getSource().equals(m_productCardMenuItem))
            m_popUpHelper.newProductCard();
        else
            m_popUpHelper.exit();

    }
}
