package com.alcanl.app.helper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@Slf4j
@Component
@Lazy
@PropertySource(value = "classpath:values.properties", encoding = "UTF-8")
public final class Resources {

    @Value("${kismet.auto.stock.tracking.system.double.threshold}")
    private double m_doubleThreshold;

    @Value("${kismet.auto.stock.tracking.system.nimbus.theme}")
    private String m_nimbusTheme;

    @Value("${kismet.auto.stock.tracking.system.default.icon}")
    private String m_defaultIconPath;

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
    private String m_errorMessageEmptyName;

    @Value("${kismet.auto.stock.tracking.system.dialog.message.stock.amount}")
    private String m_dialogStockInputText;

    @Value("${kismet.auto.stock.tracking.system.warning.message.no.selected.item}")
    private String m_warningNoSelectedItemText;

    @Value("${kismet.auto.stock.tracking.system.warning.message.delete.item}")
    private String m_warningDeleteItemText;

    @Value("${kismet.auto.stock.tracking.system.title.amount}")
    private String m_warningTitleAmount;

    @Value("${kismet.auto.stock.tracking.system.warning.message.wrong.username.or.password}")
    private String m_warningWrongUsernameOrPasswordText;

    public static final String EMPTY_STRING = "";

    private final ApplicationContext m_applicationContext;
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
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (m_nimbusTheme.equals(info.getName()))
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                         IllegalAccessException ex) {
                    JOptionPane.showMessageDialog(null, m_errorMessageUnknown, m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
                }
        }
    }
    public void centerFrame(JFrame frame)
    {
        var x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - frame.getSize().width / 2;
        var y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - frame.getSize().height / 2;
        frame.setLocation(x, y);
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
    public void showEmptyNameTextErrorMessageDialog()
    {
        JOptionPane.showMessageDialog(null, m_errorMessageEmptyName, m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
    }
    public void showUnknownErrorMessageDialog(String errMessage)
    {
        JOptionPane.showMessageDialog(null, m_errorMessageUnknown + "\n" + errMessage,
                m_errorMessageTitle, JOptionPane.ERROR_MESSAGE);
    }
    public int showAmountInputDialog()
    {
        var amount = JOptionPane.showInputDialog(null,m_dialogStockInputText, m_warningTitleAmount,
                JOptionPane.PLAIN_MESSAGE, null, null, 1);
        if (amount == null)
            return -1;
        try {
            var amountInt = Integer.parseInt((String)amount);
            if (amountInt <= 0) {
                showUnsupportedFormatWarningMessageDialog();
                return -1;
            }
            else
                return amountInt;
        } catch (NumberFormatException ignore) {
            showUnsupportedFormatWarningMessageDialog();
            return -1;
        }
    }

    public void showNoSelectedMaterialMessage()
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
    public String showCustomInputWarningDialog(String message)
    {
        return JOptionPane.showInputDialog(null, message, m_warningTitle, JOptionPane.WARNING_MESSAGE);
    }
    public void showNoSuchUserWarningDialog()
    {
        JOptionPane.showMessageDialog(null, m_warningWrongUsernameOrPasswordText, m_warningTitle, JOptionPane.WARNING_MESSAGE);
    }
}
