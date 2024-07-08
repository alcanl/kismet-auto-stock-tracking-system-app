package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.InputRecord;
import com.alcanl.app.repository.entity.OutputRecord;
import com.alcanl.app.repository.entity.Product;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(prefix = "m_")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class StockDTO {

    @EqualsAndHashCode.Include
    private long m_stockId;

    @ToString.Include
    private int m_amount;

    @EqualsAndHashCode.Include
    private String m_shelfNumber;

    @EqualsAndHashCode.Include
    private Product m_product;

    private Set<InputRecord> m_inputRecords;

    private Set<OutputRecord> m_outputRecords;
}
