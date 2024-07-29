package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.UserRecords;
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

    private UserRecords m_userRecords;

    private Product m_productOutput;

    @Override
    public String toString()
    {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(m_recordDate);
    }
}
