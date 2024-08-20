package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.entity.User;
import com.alcanl.app.repository.entity.type.StockMovementType;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(prefix = "m_")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementDTO {

    private long m_stockMovementId;

    private int m_amount;

    private LocalDate m_recordDate = LocalDate.now();

    private StockMovementType m_stockMovementType;

    private Stock m_stock;

    private User m_user;

}
