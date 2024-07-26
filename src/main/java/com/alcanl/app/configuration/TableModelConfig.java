package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.swing.table.DefaultTableModel;

@Lazy
@Configuration
public class TableModelConfig {
    @Bean
    public DefaultTableModel createTableModel()
    {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
