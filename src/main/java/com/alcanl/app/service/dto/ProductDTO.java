package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.InputRecord;
import com.alcanl.app.repository.entity.OutputRecord;
import com.alcanl.app.repository.entity.ProductRegisterRecord;
import com.alcanl.app.repository.entity.Stock;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.File;
import java.time.LocalDate;
import java.util.Set;

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

    private String m_description;

    private ProductRegisterRecord m_productRegisterRecord;

    private Set<InputRecord> m_inputRecords;

    private Set<OutputRecord> m_outputRecords;

    @Override
    public String toString()
    {
        return m_productName;
    }
}
