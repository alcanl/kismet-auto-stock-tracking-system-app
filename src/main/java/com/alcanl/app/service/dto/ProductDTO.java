package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Stock;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.File;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(prefix = "m_")
public class ProductDTO {

    @EqualsAndHashCode.Include
    private String m_originalCode;

    private String m_stockCode;

    private LocalDate m_registerDate;

    @EqualsAndHashCode.Include
    private String m_productName;

    private File m_imageFile;

    private Stock m_stock;

    @Override
    public String toString()
    {
        return m_productName;
    }
}
