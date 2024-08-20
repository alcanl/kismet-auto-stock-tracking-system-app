package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.swing.table.DefaultTableModel;

@Lazy
@Configuration
public class TableModelConfig {
    @Bean("bean.table.model.default")
    @Scope("prototype")
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
