package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.entity.User;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@Accessors(prefix = "m_")
public class OutputRecordDTO {

    private long m_outputRecordId;

    @ToString.Include
    private int m_amount;

    @ToString.Include
    private LocalDate m_recordDate;

    private User m_user;

    private Stock m_stock;
}
