package com.alcanl.app.helper.popup;

import com.alcanl.app.helper.dialog.DialogHelper;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;

@RequiredArgsConstructor
@Component
public final class PopUpHelper {

    private final DialogHelper m_dialogHelper;
    public static final String FAST_STOCK_ADD_TEXT = "Hızlı Stok Ekle";
    public static final String FAST_STOCK_RELEASE_TEXT = "Hızlı Stok Düş";
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
        if (m_dialogHelper.showEnsureExitMessageDialog() == JOptionPane.YES_OPTION)
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
        m_dialogHelper.showReleaseFastStockDialog();
    }
    public void newStockOutputWithProduct()
    {
        m_dialogHelper.showReleaseFastStockDialogWithProduct();
    }
    public void newProductCardWithProduct()
    {
        m_dialogHelper.showProductCardDialogWithProduct();
    }
    public void newProductCard()
    {
        m_dialogHelper.showNewProductCardDialog();
    }
    public void editProduct()
    {
        m_dialogHelper.showNewEditProductDialog();
    }
    public void editUser()
    {
        m_dialogHelper.showNewEditUserDialog();
    }
    public void lightTheme()
    {
        FlatLightLaf.setup();
        FlatDarkLaf.updateUI();
    }
    public void darkTheme()
    {
        FlatDarkLaf.setup();
        FlatDarkLaf.updateUI();
    }
    public void deleteSelectedProduct()
    {
        m_dialogHelper.deleteSelectedProduct();
    }
    public void editSelectedProduct()
    {
        m_dialogHelper.showEditProductDialogWithProduct();
    }
}
