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
    public ImageIcon createMaximizePartialWindowIcon()
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
    public ImageIcon createMaximizeFulllWindowIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_maximize_full.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Lazy
    @Bean(name = "bean.image.icon.right.bar.open")
    public ImageIcon createOpenBarIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_open_right_bar.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Lazy
    @Bean(name = "bean.image.icon.right.bar.close")
    public ImageIcon createCloseBarIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_close_right_bar.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Lazy
    @Bean(name = "bean.image.icon.info.success.tick")
    public ImageIcon createSuccessTickIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_success_tick.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Lazy
    @Bean(name = "bean.image.icon.dialog.add.new.product")
    public ImageIcon createAddNewProductDialogIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_dialog_add_new_product.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Lazy
    @Bean(name = "bean.image.icon.dialog.card.product")
    public ImageIcon createProductCardDialogIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_dialog_card.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Lazy
    @Bean(name = "bean.image.icon.dialog.add.stock")
    public ImageIcon createAddStockDialogIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_dialog_add_stock.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Lazy
    @Bean(name = "bean.image.icon.dialog.release.stock")
    public ImageIcon createReleaseStockDialogIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_dialog_release_stock.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Lazy
    @Bean(name = "bean.image.icon.dialog.edit.product")
    public ImageIcon createEditProductDialogIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_dialog_edit.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Lazy
    @Bean(name = "bean.image.icon.dialog.search.product")
    public ImageIcon createSearchProductDialogIcon()
    {
        try {
            return new ImageIcon(m_applicationContext.getResource("icons/icon_dialog_search.png")
                    .getContentAsByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
