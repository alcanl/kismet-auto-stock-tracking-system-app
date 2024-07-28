package com.alcanl.app.application.ui.view.popup;

import com.alcanl.app.helper.PopUpHelper;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.alcanl.app.helper.PopUpHelper.DARK_THEME_TEXT;
import static com.alcanl.app.helper.PopUpHelper.LIGHT_THEME_TEXT;

@Component("bean.menu.popup.top.bar.settings")
public class TopBarSettingsButtonPopUpMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem m_darkThemeMenuItem;
    private final JMenuItem m_lightThemeMenuItem;
    private final PopUpHelper m_popUpHelper;

    public TopBarSettingsButtonPopUpMenu(PopUpHelper popUpHelper)
    {
        m_popUpHelper = popUpHelper;
        m_darkThemeMenuItem = new JMenuItem(DARK_THEME_TEXT);
        m_lightThemeMenuItem = new JMenuItem(LIGHT_THEME_TEXT);
        initialize();
    }
    private void initialize()
    {
        m_darkThemeMenuItem.addActionListener(this);
        m_lightThemeMenuItem.addActionListener(this);
        add(m_lightThemeMenuItem);
        add(m_darkThemeMenuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == m_lightThemeMenuItem)
            m_popUpHelper.lightTheme();
        else
            m_popUpHelper.darkTheme();
    }
}
