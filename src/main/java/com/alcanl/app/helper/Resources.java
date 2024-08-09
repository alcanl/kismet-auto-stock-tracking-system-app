package com.alcanl.app.helper;

import com.alcanl.app.application.ui.view.form.MainForm;
import com.formdev.flatlaf.FlatLightLaf;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@Lazy
@PropertySource(value = "classpath:values.properties", encoding = "UTF-8")
public final class Resources {

    @Value("${kismet.auto.stock.tracking.system.warning.message.sure.to.logout}")
    private String m_warningMessageSureToLogout;

    @Value("${kismet.auto.stock.tracking.system.default.logo}")
    private String m_defaultLogoPath;

    @Value("${kismet.auto.stock.tracking.system.error.message.unknown}")
    private String m_errorMessageUnknown;

    @Value("${kismet.auto.stock.tracking.system.warning.message.sure.to.exit}")
    private String m_warningMessageSureToExit;

    @Value("${kismet.auto.stock.tracking.system.error.title}")
    private String m_errorMessageTitle;

    @Value("${kismet.auto.stock.tracking.system.warning.title}")
    private String m_warningTitle;

    @Value("${kismet.auto.stock.tracking.system.error.message.unsupported.format}")
    private String m_errorUnsupportedFormat;

    @Value("${kismet.auto.stock.tracking.system.warning.message.empty.search.list}")
    private String m_warningEmptySearchList;

    @Value("${kismet.auto.stock.tracking.system.error.message.empty.name}")
    private String m_errorMessageEmptyEntry;

    @Value("${kismet.auto.stock.tracking.system.warning.message.no.selected.item}")
    private String m_warningNoSelectedItemText;

    @Value("${kismet.auto.stock.tracking.system.warning.message.delete.item}")
    private String m_warningDeleteItemText;

    @Value("${kismet.auto.stock.tracking.system.info.title}")
    private String m_infoMessageTitle;

    @Value("${kismet.auto.stock.tracking.system.warning.message.wrong.username.or.password}")
    private String m_warningWrongUsernameOrPasswordText;

    public static final String EMPTY_STRING = "";

    private final ApplicationContext m_applicationContext;

    private void setTextFont(Font font, JComponent jComponent)
    {
        for (var component : jComponent.getComponents())
            if (component instanceof JLabel || component instanceof JButton || component instanceof JCheckBox
                    || component instanceof JTextComponent || component instanceof JComboBox)
                component.setFont(font);

            else if (component instanceof JScrollPane jScrollPane) {
                Arrays.stream(jScrollPane.getViewport().getComponents()).forEach(c -> setTextFont(font, (JComponent)c));
            }
            else if (component instanceof JPanel || component instanceof JTabbedPane)
                setTextFont(font, (JComponent)component);
    }
    public Resources(ApplicationContext applicationContext)
    {
        m_applicationContext = applicationContext;
    }

    @PostConstruct
    public void initializeDialogLanguage()
    {
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.cancelButtonText", "İptal");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }

    public void setLayout()
    {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (UnsupportedLookAndFeelException  ex) {
            JOptionPane.showMessageDialog(null, m_errorMessageUnknown, m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initializeLogo(JFrame frame)
    {
        try {
            var icon = new ImageIcon(m_applicationContext.getResource(m_defaultLogoPath).getContentAsByteArray());
            frame.setIconImage(icon.getImage());
        } catch (IOException ex) {
            log.error("Resources::initializeLogo: {}", ex.getMessage());
        }
    }
    public void showUnsupportedFormatWarningMessageDialog()
    {
        JOptionPane.showMessageDialog(null, m_errorUnsupportedFormat, m_errorMessageTitle,
                JOptionPane.ERROR_MESSAGE);
    }
    public void showEmptyListWarningMessageDialog()
    {
        JOptionPane.showMessageDialog(null, m_warningEmptySearchList, m_warningTitle,
                JOptionPane.INFORMATION_MESSAGE);

    }
    public int showEnsureWarningMessageDialog()
    {
        return JOptionPane.showConfirmDialog(null, m_warningDeleteItemText, m_warningTitle,
                JOptionPane.YES_NO_OPTION);
    }
    public int showEnsureExitMessageDialog()
    {
        return JOptionPane.showConfirmDialog(null, m_warningMessageSureToExit, m_warningTitle,
                JOptionPane.YES_NO_OPTION);
    }
    public int showEnsureLogoutMessageDialog(String userInfo)
    {
        return JOptionPane.showConfirmDialog(null,
                String.format(m_warningMessageSureToLogout, userInfo), m_warningTitle,
                JOptionPane.YES_NO_OPTION);
    }
    public void showEmptyProductFieldTextErrorMessageDialog()
    {
        JOptionPane.showMessageDialog(null, m_errorMessageEmptyEntry, m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
    }
    public void showUnknownErrorMessageDialog(String errMessage)
    {
        JOptionPane.showMessageDialog(null, m_errorMessageUnknown + "\n" + errMessage,
                m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
    }

    public void showNoSelectedProductMessage()
    {
        JOptionPane.showMessageDialog(null, m_warningNoSelectedItemText, m_warningTitle,
                JOptionPane.WARNING_MESSAGE);
    }
    public void showCustomWarningDialog(String message)
    {
        JOptionPane.showMessageDialog(null, message, m_warningTitle, JOptionPane.WARNING_MESSAGE);
    }
    public void showCustomErrorDialog(String message)
    {
        JOptionPane.showMessageDialog(null, message, m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
    }
    public void showCustomInfoDialog(String message)
    {
        JOptionPane.showMessageDialog(null, message, m_infoMessageTitle, JOptionPane.INFORMATION_MESSAGE);
    }
    public void showNoSuchUserWarningDialog()
    {
        JOptionPane.showMessageDialog(null, m_warningWrongUsernameOrPasswordText, m_warningTitle, JOptionPane.WARNING_MESSAGE);
    }
    public synchronized void setTextFont(Font font)
    {
        var form = m_applicationContext.getBean("bean.form.main", MainForm.class);
        setTextFont(font, form.getPanelMain());
        form.getLabelCount().setFont(new Font("calibri", Font.BOLD, 14));
    }

}
