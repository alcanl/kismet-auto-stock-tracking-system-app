package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.alcanl.app.repository.exception.EmailAlreadyInUseException;
import com.alcanl.app.service.dto.UserDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.*;

@Slf4j
@SwingContainer
@Component("bean.dialog.edit.user")
@Scope("prototype")
@Lazy
@RequiredArgsConstructor
public class DialogEditUser extends JDialog {
    private JPanel contentPaneMain;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldUserName;
    private JTextField textFieldFirstName;
    private JTextField textFieldLastName;
    private JTextField textFieldEMail;
    private JTextArea textFieldDescription;
    private JCheckBox checkBoxIsAdmin;
    private final DialogHelper m_dialogHelper;
    private final ApplicationContext m_applicationContext;
    private static final String ms_title = "Kullanıcı Düzenle";

    @PostConstruct
    private void postConstruct()
    {
        m_dialogHelper.initializeDialog(this, contentPaneMain, ms_title, buttonOK,
                m_applicationContext.getBean("bean.image.icon.dialog.edit.user", ImageIcon.class));
        initializeButtons();
        initializeTextFields();
        registerKeys();
        m_dialogHelper.disableTextAreaGrowthBehaviour(textFieldDescription);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        dispose();
    }
    private void initializeTextFields()
    {
        var user = m_dialogHelper.getSelectedUser();
        textFieldUserName.setText(user.getUsername());
        textFieldFirstName.setText(user.getFirstName());
        textFieldLastName.setText(user.getLastName());
        textFieldEMail.setText(user.getEMail());
        textFieldDescription.setText(user.getDescription());
        checkBoxIsAdmin.setSelected(user.isAdmin());
    }

    private void initializeButtons()
    {
        buttonOK.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);
    }
    private void registerKeys()
    {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        contentPaneMain.registerKeyboardAction(this::onCancel,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPaneMain.registerKeyboardAction(this::onOK,
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(ActionEvent event)
    {
        var firstName = textFieldFirstName.getText().trim();
        var lastName = textFieldLastName.getText().trim();
        var email = textFieldEMail.getText().trim();
        var description = textFieldDescription.getText().trim();
        var isAdmin = checkBoxIsAdmin.isSelected();

        if (m_dialogHelper.isThereAnyFieldEmpty(firstName, lastName, email, description)) {
            m_dialogHelper.showEmptyUserFieldsWarningDialog();
            return;
        }

        if (m_dialogHelper.isInvalidEMail(email)) {
            m_dialogHelper.showUnSupportedFormatMessage("%s".formatted(email));
            return;
        }

        var userDTO = new UserDTO();
        userDTO.setUserId(m_dialogHelper.getSelectedUser().getUserId());
        userDTO.setUsername(m_dialogHelper.getSelectedUser().getUsername());
        userDTO.setAdmin(isAdmin);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setDescription(description);
        userDTO.setEMail(email);

        try {
            m_dialogHelper.editUser(userDTO);
            m_dialogHelper.showSaveUserProcessSuccessInfoDialog();
        }
        catch (EmailAlreadyInUseException ex) {
            m_dialogHelper.showCustomErrorMessageDialog(ex.getMessage());
        }
        catch (ServiceException e) {
            m_dialogHelper.showUnknownErrorMessageDialog(e.getMessage());
        }

        dispose();
    }

    private void onCancel(ActionEvent event)
    {
        dispose();
    }
}
