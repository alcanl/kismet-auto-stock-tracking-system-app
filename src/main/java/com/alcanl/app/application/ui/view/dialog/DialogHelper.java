package com.alcanl.app.application.ui.view.dialog;

import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.helper.Resources;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public final class DialogHelper {
    private final Resources m_resources;
    private final ApplicationEventPublisher m_applicationEventPublisher;

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
        m_resources.showCustomErrorDialog("%s Orijinal Koduna Sahip Ürün Daha Önceden Kaydedilmiştir.".formatted(originalCode));
    }
    public void showProductSaveSuccess()
    {
        m_resources.showCustomInfoDialog("Ürün Kaydedildi.");
    }
    public void showUnSupportedFormatMessage(String format)
    {
        m_resources.showCustomErrorDialog("Kayıt için Geçersiz Formatte Bir Değer Girdiniz : %s".formatted(format));
    }
    public void notifyTables()
    {
        m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
    }
}
