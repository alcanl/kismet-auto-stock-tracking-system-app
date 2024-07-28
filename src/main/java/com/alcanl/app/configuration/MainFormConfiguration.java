package com.alcanl.app.configuration;

import com.alcanl.app.application.ui.view.form.MainForm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MainFormConfiguration {

    @Bean("bean.form.main")
    @Lazy
    public MainForm createLoginForm()
    {
        var form = new MainForm();
        form.setContentPane(form.getPanelMain());
        return form;
    }
}
