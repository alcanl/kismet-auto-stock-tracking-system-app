package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Product;
import lombok.*;
import lombok.experimental.Accessors;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(prefix = "m_")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StockDTO {

    @EqualsAndHashCode.Include
    private long m_stockId;

    private int m_amount;

    private int m_threshold;

    @EqualsAndHashCode.Include
    private String m_shelfNumber;

    @EqualsAndHashCode.Include
    private Product m_product;

    @Override
    public String toString()
    {
        return "%d".formatted(m_amount);
    }
}
