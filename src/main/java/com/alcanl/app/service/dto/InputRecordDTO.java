package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.UserRecords;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Accessors(prefix = "m_")
public class InputRecordDTO {

    private long m_inputRecordId;

    private int m_amount;

    private LocalDate m_recordDate = LocalDate.now();

    private UserRecords m_userRecords;

    private Product m_productInput;

    @Override
    public String toString()
    {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(m_recordDate);
    }
}
