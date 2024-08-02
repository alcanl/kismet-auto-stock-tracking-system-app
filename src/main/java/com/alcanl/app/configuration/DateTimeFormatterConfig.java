package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeFormatterConfig {
    @Bean("bean.date.time.formatter")
    public DateTimeFormatter createFormatter()
    {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
}
