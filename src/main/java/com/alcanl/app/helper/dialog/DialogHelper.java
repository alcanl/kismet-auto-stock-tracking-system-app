package com.alcanl.app.helper.dialog;

import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.application.ui.view.dialog.*;
import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.helper.font.FontChangeHelper;
import com.alcanl.app.helper.Resources;
import com.alcanl.app.repository.entity.StockMovement;
import com.alcanl.app.repository.entity.UpdateOperation;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.repository.entity.type.UpdateOperationType;
import com.alcanl.app.service.ApplicationService;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
import com.alcanl.app.service.dto.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import static com.alcanl.app.helper.Resources.EMPTY_STRING;

@Component
@RequiredArgsConstructor
public final class DialogHelper {

    @Getter
    @Setter
    @Accessors(prefix = "m_")
    private ProductDTO m_selectedProduct;
    @Getter
    @Setter
    @Accessors(prefix = "m_")
    private UserDTO m_selectedUser;
    private final Resources m_resources;
    private final FontChangeHelper m_fontChangeHelper;
    private final ApplicationEventPublisher m_applicationEventPublisher;
    private final ApplicationContext m_applicationContext;
    private final ApplicationService m_applicationService;
    private final PrinterJob m_printerJob;
    private final CurrentUserConfig m_currentUserConfig;

    private void clearFields(JComponent jComponent)
    {
        for (var component : jComponent.getComponents())
            if (component instanceof JPanel jPanel)
                clearFields(jPanel);
            else if (component instanceof JScrollPane jScrollPane)
                Arrays.stream(jScrollPane.getViewport().getComponents()).forEach(c -> clearFields((JComponent)c ));
            else if (component instanceof JTextComponent jTextComponent)
                jTextComponent.setText(EMPTY_STRING);

    }
    public boolean areProductFieldsValid(String... varargs)
    {
        if (Arrays.stream(varargs).anyMatch(String::isBlank)) {
            m_resources.showEmptyProductFieldTextErrorMessageDialog();
            return false;
        }
        return true;
    }
    public boolean isThereAnyFieldEmpty(String... varargs)
    {
        return Arrays.stream(varargs).anyMatch(String::isEmpty)
                && Arrays.stream(varargs).anyMatch(it -> it.contains(" "));
    }
    public void disableTextAreaGrowthBehaviour(JTextArea jTextArea)
    {
        jTextArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        jTextArea.setFocusTraversalKeys (KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));

        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null || jTextArea.getText().length() >= 255)
                    return;

                super.insertString(offs, str, a);
            }
        });
    }
    public void showUnknownErrorMessageWhileSavingProduct()
    {
        m_resources.showUnknownErrorMessageDialog("Ürün Kaydedilirken Bilinmeyen Bir Hata Oluştu.");
    }
    public void showProductAlreadyExistMessage(String originalCode)
    {
        m_resources.showCustomErrorDialog("%s Orjinal Koduna Sahip Ürün Daha Önceden Kaydedilmiştir.".formatted(originalCode));
    }
    public void showProductSaveSuccess(String originalCode)
    {
        m_resources.showCustomInfoDialog("%s Orjinal Kodlu Ürün Kaydedildi.".formatted(originalCode));
    }
    public void showUnSupportedFormatMessage(String format)
    {
        m_resources.showCustomErrorDialog("Kayıt İçin Geçersiz Formatta Bir Değer Girdiniz : %s".formatted(format));
    }
    public void notifyTables()
    {
        m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
    }
    public void showSaveUserProcessSuccessInfoDialog()
    {
        m_resources.showCustomInfoDialog("İşlem Başarılı");
        notifyTables();
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
        m_resources.showCustomWarningDialog("Seçili Bir Ürün Bulunmamaktadır. Lütfen Devam Etmek İçin Listeden İlgili Ürünü Seçiniz.");
    }
    public void showNoSelectedSearchMessage()
    {
        m_resources.showCustomWarningDialog("Seçili Bir Arama Kriteri Bulunmamaktadır. Lütfen Devam Etmek İçin Bir Kriter Seçiniz.");
    }
    public void showNoSelectedUserMessage()
    {
        m_resources.showCustomWarningDialog("Seçili Bir Kullanıcı Bulunmamaktadır. Lütfen Devam Etmek İçin Bir Kullanıcı Seçiniz.");
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
    public StockMovement saveNewStockMovementWithProductCreate(int stockAmount, int stockThreshold, String productShelfCode, String productOriginalCode,
                                                               String stockCode, String productName, File imageFile, String description) throws ExecutionException, InterruptedException {
        var stockDTO = new StockDTO();
        stockDTO.setAmount(stockAmount);
        stockDTO.setThreshold(stockThreshold);
        stockDTO.setShelfNumber(productShelfCode);

        var productDTO = new ProductDTO( productOriginalCode, stockCode, LocalDate.now(), productName,
                imageFile, null, description);

        return m_applicationService.saveNewStockMovement(new StockMovementDTO(), stockDTO, productDTO);
    }
    public UpdateOperation saveNewUpdateOperation(int stockAmount, int stockThreshold, String productShelfCode, String productOriginalCode,
                                                  String stockCode, String productName, File imageFile, String description,
                                                  UpdateOperationType updateOperationType) throws ExecutionException, InterruptedException
    {
        var productDTO = m_applicationService.findProductById(productOriginalCode);

        if (productDTO.isPresent()) {

            productDTO.map(dto -> {
               dto.setDescription(description);
               dto.setImageFile(imageFile);
               dto.setProductName(productName);
               dto.setStockCode(stockCode);
               dto.getStock().setShelfNumber(productShelfCode);
               dto.getStock().setAmount(stockAmount);
               dto.getStock().setThreshold(stockThreshold);
               return dto;
            });

            return m_applicationService.updateProduct(productDTO.get(), updateOperationType);
        }
        return null;
    }
    public void showNewProductCardDialog()
    {
        m_applicationContext.getBean("bean.dialog.card.product.search", DialogProductSearch.class)
                .setVisible(true);
    }
    public void showNewEditProductDialog()
    {
        m_applicationContext.getBean("bean.dialog.product.search.for.edit", DialogProductSearchForEdit.class)
                .setVisible(true);
    }
    public void showEditProductDialogWithProduct()
    {
        m_applicationContext.getBean("bean.dialog.edit.product", DialogEditProduct.class)
                .setVisible(true);
    }
    public void showNewEditUserDialog()
    {
        if (!m_currentUserConfig.getUser().isAdmin()) {
            showNoAuthorizationWarningDialog();
            return;
        }

        if (m_selectedUser == null) {
            showNoSelectedUserMessage();
            return;
        }

        m_applicationContext.getBean("bean.dialog.edit.user", DialogEditUser.class).setVisible(true);
    }
    public void showEmptyFieldsWarningDialog()
    {
        m_resources.showCustomWarningDialog("Seçili Kriterlerde Arama Yapmak İçin Zorunlu Alanlar Boş Bırakılamaz!");
    }
    public void showEmptyUserFieldsWarningDialog()
    {
        m_resources.showCustomWarningDialog("Kullanıcı Kaydı İçin Zorunlu Alanlar Boş Bırakılamaz!");
    }
    public void showEmptyFilteredListWarningDialog()
    {
        m_resources.showEmptyListWarningMessageDialog();
    }
    public int showEnsureDeleteItemWarningDialog()
    {
        return m_resources.showEnsureWarningMessageDialog();
    }
    public int showEnsureDeleteUserWarningDialog()
    {
        return m_resources.showEnsureWarningDeleteUserMessageDialog();
    }
    public void centerFrame(JFrame frame)
    {
        var x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - frame.getSize().width / 2;
        var y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - frame.getSize().height / 2;
        frame.setLocation(x, y);
    }
    public void setLayout()
    {
        m_resources.setLayout();
    }
    public void initializeLogo(JFrame frame)
    {
        m_resources.initializeLogo(frame);
    }
    public int showEnsureExitMessageDialog()
    {
        return m_resources.showEnsureExitMessageDialog();
    }
    public int showEnsureLogoutMessageDialog(String userInfo)
    {
        return m_resources.showEnsureLogoutMessageDialog(userInfo);
    }
    public void showUnknownErrorMessageDialog(String errMessage)
    {
        m_resources.showUnknownErrorMessageDialog(errMessage);
    }
    public void showCustomErrorMessageDialog(String errMessage)
    {
        m_resources.showCustomErrorDialog(errMessage);
    }
    public void showNoSuchUserWarningDialog()
    {
        m_resources.showNoSuchUserWarningDialog();
    }
    public int showEnsureWarningMessageDialog()
    {
        return m_resources.showEnsureWarningMessageDialog();
    }
    public void showNoAuthorizationWarningDialog()
    {
        m_resources.showCustomWarningDialog("Bu İşlem İçin Yetkiniz Bulunmamaktadır.");
    }
    public void deleteSelectedProduct()
    {
        if (!m_currentUserConfig.getUser().isAdmin()) {
            showNoAuthorizationWarningDialog();
            return;
        }

        try {
            if (showEnsureWarningMessageDialog() == JOptionPane.YES_OPTION)
                if (m_currentUserConfig.getUser().isAdmin())
                    m_applicationService.deleteProduct(m_selectedProduct);
                else
                    showNoAuthorizationWarningDialog();

        } catch (ServiceException ex) {
            showUnknownErrorMessageDialog("Ürün Silinirken Bir Hata ile karşılaşıldı : %s".formatted(ex.getMessage()));
        }

    }
    public void setFontLarger(Font font)
    {
        m_resources.setTextFont(m_fontChangeHelper.getLargerSize(font));
    }
    public void setFontSmaller(Font font)
    {
        m_resources.setTextFont(m_fontChangeHelper.getSmallerSize(font));
    }
    public void disableComponents(JPanel jPanel)
    {
        m_resources.disableComponents(jPanel);
    }
    public void enableComponents(JPanel jPanel)
    {
        m_resources.enableComponents(jPanel);
    }
    public void showConfirmPasswordNotEqualsWarningDialog()
    {
        m_resources.showCustomWarningDialog("Hatalı Şifre!");
    }
    public void showShortLengthPasswordWarningDialog()
    {
        m_resources.showCustomWarningDialog("Şire En Az 8 Karakterden Oluşmalıdır.");
    }
    public void setPasswordFieldVisibleOrInvisibleCallback(JPasswordField passwordField ,JLabel passwordLabel)
    {
        try {
            var hiddenChar = (char)0x2022;
            if (passwordField.echoCharIsSet()) {
                passwordField.setEchoChar('\0');
                passwordLabel.setIcon(m_applicationContext.getBean("bean.image.icon.password.field.visible",
                        ImageIcon.class));
            }
            else {
                passwordField.setEchoChar(hiddenChar);
                passwordLabel.setIcon(m_applicationContext.getBean("bean.image.icon.password.field.hidden",
                        ImageIcon.class));
            }
        } catch (RuntimeException ex) {
            showUnknownErrorMessageDialog(ex.getMessage());
        }
    }
    public boolean isInvalidEMail(String eMail)
    {
        return !m_resources.isValidEmail(eMail);
    }
    public void deleteUser()
    {
        if (!m_currentUserConfig.getUser().isAdmin()) {
            showNoAuthorizationWarningDialog();
            return;
        }

        if (m_currentUserConfig.getUser().getUsername().equals(m_selectedUser.getUsername())){
            m_resources.showCustomWarningDialog("Hesap Silme İşlemi Aktif Yönetici Tarafından Gerçekleştirilemez!");
            return;
        }

        if (showEnsureDeleteUserWarningDialog() == JOptionPane.YES_OPTION) {
            m_applicationService.deleteUser(m_selectedUser);
            m_resources.showCustomInfoDialog("Kullanıcı Kaydı Silindi");
            m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
        }
    }
    public void clearFields(JPanel... jPanels)
    {
        Arrays.stream(jPanels).forEach(this::clearFields);
    }
    public void editUser(UserDTO userDTO)
    {
        m_applicationService.updateUser(userDTO);
        m_resources.showCustomInfoDialog("Kayıt Güncellendi");
        m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
    }
}
