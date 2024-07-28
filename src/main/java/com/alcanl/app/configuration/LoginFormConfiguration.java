package com.alcanl.app.configuration;

import com.alcanl.app.application.ui.view.form.LoginForm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class LoginFormConfiguration {

    @Bean("bean.form.login")
    @Lazy
    public LoginForm createLoginForm()
    {
        var form = new LoginForm();
        form.setContentPane(form.getPanelMain());
        return form;
    }
}
