package com.alcanl.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.awt.print.PrinterJob;

@Lazy
@Configuration
@Scope("prototype")
public class PrinterJobConfig {
    @Bean
    @Lazy
    @Scope("prototype")
    public PrinterJob createPrinterJob()
    {
        return PrinterJob.getPrinterJob();
    }
}
