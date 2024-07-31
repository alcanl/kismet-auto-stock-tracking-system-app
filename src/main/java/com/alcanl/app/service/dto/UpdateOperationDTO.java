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
public class UpdateOperationDTO {

    private long m_updateOperationId;

    private LocalDate m_recordDate = LocalDate.now();

    private StockMovementType m_updateOperationType;

    private Stock m_stock;

    private User m_user;

}
