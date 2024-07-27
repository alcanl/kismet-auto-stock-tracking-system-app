package com.alcanl.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

@Configuration
@Scope("prototype")
public class JFileChooserConfig {

    @Value("${kismet.auto.stock.tracking.system.warning.message.valid.format.image.file}")
    private String m_invalidFormatFileDescription;

    @Bean
    @Scope("prototype")
    public JFileChooser createFileChooser()
    {
        var fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        var filter = new FileNameExtensionFilter(m_invalidFormatFileDescription,"jpg","png", "jpeg");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        return fileChooser;
    }
}
