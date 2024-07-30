package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.awt.print.PrinterJob;

@Lazy
@Configuration
public class PrinterJobConfig {
    @Bean
    @Lazy
    public PrinterJob createPrinterJob()
    {
        return PrinterJob.getPrinterJob();
    }
}
