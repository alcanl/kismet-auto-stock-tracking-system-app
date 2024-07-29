package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.UserRecords;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(prefix = "m_")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegisterRecordDTO {

    private Long m_productRegisterRecordId;

    private int m_amount;

    private LocalDate m_productRegisterRecordDate;

    private Product m_product;

    private UserRecords m_userRecord;
}
