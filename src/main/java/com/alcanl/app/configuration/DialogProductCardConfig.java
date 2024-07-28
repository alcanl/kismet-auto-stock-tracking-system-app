package com.alcanl.app.configuration;

import com.alcanl.app.application.ui.view.dialog.DialogProductCard;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;


@AllArgsConstructor
@Configuration
public class DialogProductCardConfig {

    @Bean("bean.dialog.card.product")
    @Scope("prototype")
    @Lazy
    public DialogProductCard createDialog()
    {
        var dialog = new DialogProductCard();
        dialog.setContentPane(dialog.getContentPaneMain());

        return dialog;
    }
}
