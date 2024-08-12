package com.alcanl.app.repository.entity.type;


public enum StockMovementType {
    STOCK_INPUT("GİRİŞ"), STOCK_OUTPUT("ÇIKIŞ"), STOCK_REGISTER("KAYIT");

    private final String m_name;
    StockMovementType(String name)
    {
        m_name = name;
    }

    @Override
    public String toString()
    {
        return m_name;
    }
}
