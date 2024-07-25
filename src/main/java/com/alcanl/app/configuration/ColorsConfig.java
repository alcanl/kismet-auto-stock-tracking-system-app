package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.awt.*;

@Lazy
@Configuration
public class ColorsConfig {

    @Bean(name = "bean.color.default")
    @Lazy
    public Color createDefaultColor()
    {
        return new Color(123,132,148);
    }
    @Bean(name = "bean.color.button.exit")
    @Lazy
    public Color createExitButtonColor()
    {
        return new Color(204, 35, 56);
    }
}
