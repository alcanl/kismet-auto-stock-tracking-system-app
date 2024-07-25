package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.entity.User;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Accessors(prefix = "m_")
public class OutputRecordDTO {

    private long m_outputRecordId;

    private int m_amount;

    private LocalDate m_recordDate = LocalDate.now();

    private User m_user;

    private Stock m_stock;

    @Override
    public String toString()
    {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(m_recordDate);
    }
}
