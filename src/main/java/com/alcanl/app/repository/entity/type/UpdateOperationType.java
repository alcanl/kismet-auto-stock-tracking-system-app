package com.alcanl.app.repository.entity.type;


public enum UpdateOperationType {
    STOCK_UPDATE("Stok Bilgisi Güncelleme"), PRODUCT_UPDATE("Ürün Bilgisi Güncelleme"), BOTH("Stok ve Ürün Güncelleme");
    private final String m_value;

    UpdateOperationType(String name)
    {
        m_value = name;
    }

    @Override
    public String toString()
    {
        return m_value;
    }
}
