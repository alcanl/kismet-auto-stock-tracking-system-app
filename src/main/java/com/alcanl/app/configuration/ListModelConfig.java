package com.alcanl.app.configuration;

import com.alcanl.app.service.dto.ProductDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.swing.*;

@Configuration
@Lazy
public class ListModelConfig {

    @Bean
    @Lazy
    public DefaultListModel<ProductDTO> createListModel()
    {
        return new DefaultListModel<>();
    }
}
