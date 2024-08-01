package com.alcanl.app.helper;

import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.application.ui.view.dialog.*;
import com.alcanl.app.repository.entity.StockMovement;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public final class DialogHelper {

    @Getter
    @Setter
    @Accessors(prefix = "m_")
    private ProductDTO m_selectedProduct;
    private final Resources m_resources;
    private final ApplicationEventPublisher m_applicationEventPublisher;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final PrinterJob m_printerJob;

    public boolean areFieldsValid(String... varargs)
    {
        if (Arrays.stream(varargs).anyMatch(String::isBlank)) {
            m_resources.showEmptyProductFieldTextErrorMessageDialog();
            return false;
        }
        return true;
    }
    public void showUnknownErrorMessage()
    {
        m_resources.showUnknownErrorMessageDialog("Ürün Kaydedilirken Bilinmeyen Bir Hata Oluştu.");
    }
    public void showProductAlreadyExistMessage(String originalCode)
    {
        m_resources.showCustomErrorDialog("%s Orjinal Koduna Sahip Ürün Daha Önceden Kaydedilmiştir.".formatted(originalCode));
    }
    public void showProductSaveSuccess(StockMovement stockMovement)
    {
        m_resources.showCustomInfoDialog("%s Orjinal Kodlu Ürün Kaydedildi.".formatted(stockMovement.getStock().getProduct().getOriginalCode()));
    }
    public void showUnSupportedFormatMessage(String format)
    {
        m_resources.showCustomErrorDialog("Kayıt İçin Geçersiz Formatta Bir Değer Girdiniz : %s".formatted(format));
    }
    public void notifyTables()
    {
        m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
    }
    public void showProductRegisterDialog()
    {
        m_applicationContext.getBean("bean.dialog.add.new.product", DialogAddNewProduct.class)
               .setVisible(true);
    }
    public void showAdditionFastStockDialog()
    {
         m_applicationContext.getBean("bean.dialog.fast.stock.addition", DialogFastStockAddition.class)
                 .setVisible(true);

    }
    public void showReleaseFastStockDialog()
    {
        m_applicationContext.getBean("bean.dialog.fast.stock.release", DialogFastStockRelease.class)
                .setVisible(true);

    }
    public void showAdditionFastStockDialogWithProduct()
    {
        var dialog = m_applicationContext.getBean("bean.dialog.fast.stock.addition", DialogFastStockAddition.class);
        dialog.initializeTextFields();
        dialog.setVisible(true);
    }
    public void showReleaseFastStockDialogWithProduct()
    {
        var dialog = m_applicationContext.getBean("bean.dialog.fast.stock.release", DialogFastStockRelease.class);
        dialog.initializeTextFields();
        dialog.setVisible(true);
    }
    public void showProductCardDialogWithProduct()
    {
        m_applicationContext.getBean("bean.dialog.card.product", DialogProductCard.class)
                .setVisible(true);
    }
    public void printLabel()
    {
        m_printerJob.setPrintable(m_applicationContext.getBean("bean.print.printable", Printable.class));
    }
    public void showNoSelectedProductMessage()
    {
        m_resources.showCustomWarningDialog("Seçili Bir Ürün Bulunmamaktadır. Lütfen Kayıt İçin Listeden İlgili Ürünü Seçiniz.");
    }
    public void addOrReleaseDialogOnSearchButtonClickedCallback(Vector<ProductDTO> listData, DefaultListModel<ProductDTO> listModel,
                                                                String productSearch, JList<ProductDTO> jList) throws ExecutionException, InterruptedException
    {
        listData.clear();
        listModel.clear();
        m_applicationService.findAllProductsByContains(productSearch).forEach(listModel::addElement);
        jList.setModel(listModel);
        jList.updateUI();
    }
    public void initializeDialog(JDialog dialog, Container contentPane, String title,
                                 JButton defaultButton, ImageIcon icon)
    {
        dialog.setTitle(title);
        dialog.setContentPane(contentPane);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().setDefaultButton(defaultButton);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setIconImage(icon.getImage());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });
        dialog.dispose();
    }
    public StockMovement saveNewStockMovement(ProductDTO productDTO, int stockMovementAmount, StockMovementType stockMovementType) throws ExecutionException, InterruptedException {
        var stockMovementDTO = new StockMovementDTO();
        stockMovementDTO.setStock(productDTO.getStock());
        stockMovementDTO.setStockMovementType(stockMovementType);
        stockMovementDTO.setAmount(stockMovementAmount);

        return m_applicationService.saveNewStockMovementWithUpdateItem(stockMovementDTO, productDTO);

    }
    public void showNewProductCardDialog()
    {
        m_applicationContext.getBean("bean.dialog.card.product.search", DialogProductSearch.class)
                .setVisible(true);
    }
}
