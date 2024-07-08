package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Stock;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Accessors(prefix = "m_")
public class ProductDTO {

    @ToString.Include
    @EqualsAndHashCode.Include
    private String m_originalCode;

    @ToString.Include
    private String m_stockCode;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String m_productName;

    private File m_imageFile;

    private Stock m_stock;
}
