package com.alcanl.app.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.swing.*;
import java.io.IOException;

@Configuration
@Lazy
@AllArgsConstructor
public class IconConfig {
    private final ApplicationContext m_applicationContext;

    @Lazy
    @Bean(name = "bean.image.icon.maximize.partial.window")
    public Icon createMaximizePartialWindowIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_maximize_window.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Lazy
    @Bean(name = "bean.image.icon.maximize.full.window")
    public Icon createMaximizeFulllWindowIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_maximize_full.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
