package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.entity.User;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(onlyExplicitlyIncluded = true)
@Accessors(prefix = "m_")
public class InputRecordDTO {

    private long m_inputRecordId;

    @ToString.Include
    private int m_amount;

    @ToString.Include
    private LocalDate m_recordDate;

    private User m_user;

    private Stock m_stock;
}
