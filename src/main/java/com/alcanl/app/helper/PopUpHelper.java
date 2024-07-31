package com.alcanl.app.helper;

import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;

@RequiredArgsConstructor
@Component
public final class PopUpHelper {
    private final Resources m_resources;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final DialogHelper m_dialogHelper;
    @Getter
    @Setter
    @Accessors(prefix = "m_")
    private ProductDTO m_selectedProduct;

    public static final String FAST_STOCK_ADD_TEXT = "Hızlı Stok Ekle";
    public static final String EDIT_PRODUCT_TEXT = "Ürün Düzenle";
    public static final String DELETE_PRODUCT_TEXT = "Ürün Kaydını Sil";
    public static final String GET_PRODUCT_CART_TEXT = "Ürün Kartını Getir";
    public static final String NEW_PRODUCT_TEXT = "Yeni Ürün";
    public static final String EDIT_USER_TEXT = "Kullanıcı Düzenle";
    public static final String EXIT_TEXT = "Çıkış";
    public static final String NEW_STOCK_INPUT_TEXT = "Stok Girişi";
    public static final String NEW_STOCK_OUTPUT_TEXT = "Stok Çıkışı";
    public static final String NEW_PRODUCT_CARD_TEXT = "Yeni Ürün Etiketi";
    public static final String DARK_THEME_TEXT = "Karanlık Mod";
    public static final String LIGHT_THEME_TEXT = "Aydınlık Mod";

    public void exit()
    {
        if (m_resources.showEnsureExitMessageDialog() == JOptionPane.YES_OPTION)
            System.exit(0);

    }
    public void newProduct()
    {
        m_dialogHelper.showProductRegisterDialog();
    }
    public void newStockInputWithProduct()
    {
        m_dialogHelper.showAdditionFastStockDialogWithProduct();
    }
    public void newStockInput()
    {
        m_dialogHelper.showAdditionFastStockDialog();
    }
    public void newStockOutput()
    {

    }
    public void newStockOutputWithProduct()
    {

    }
    public void newProductCardWithProduct()
    {
        m_dialogHelper.showProductCardDialogWithProduct();
    }
    public void newProductCard()
    {

    }
    public void editProduct()
    {

    }
    public void editUser()
    {

    }
    public void lightTheme()
    {

    }
    public void darkTheme()
    {

    }
    public void deleteSelectedProduct()
    {
        try {
            if (m_resources.showEnsureWarningMessageDialog() == JOptionPane.YES_OPTION)
                m_applicationService.deleteProduct(m_selectedProduct.getOriginalCode());

        } catch (ServiceException ex) {
            m_resources.showCustomErrorDialog("Ürün Silinirken Bir Hata ile karşılaşıldı : %s".formatted(ex.getMessage()));
        }

    }
    public void editSelectedProduct()
    {

    }
}
