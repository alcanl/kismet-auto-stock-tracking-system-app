package com.alcanl.app.configuration;

import com.alcanl.app.application.ui.view.dialog.DialogAddNewProduct;
import com.alcanl.app.application.ui.view.dialog.DialogHelper;
import com.alcanl.app.service.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.swing.*;

@AllArgsConstructor
@Configuration
public class DialogAddNewProductConfig {
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final DialogHelper m_dialogHelper;
    private final JFileChooser m_fileChooser;

    @Bean("bean.dialog.add.new.product")
    @Scope("prototype")
    @Lazy
    public DialogAddNewProduct createDialog()
    {
        var dialog = new DialogAddNewProduct(m_applicationService, m_fileChooser, m_dialogHelper, m_applicationContext);
        dialog.setContentPane(dialog.getContentPaneMain());

        return dialog;
    }
}
