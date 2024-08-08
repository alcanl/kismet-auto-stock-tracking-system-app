package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.util.HashMap;

@Configuration
public class ComponentMapConfig {

    @Bean
    public HashMap<Component, String> createComponentMap()
    {
        return new HashMap<>();
    }
}
