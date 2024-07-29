package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.InputRecord;
import com.alcanl.app.repository.entity.OutputRecord;
import com.alcanl.app.repository.entity.ProductRegisterRecord;
import com.alcanl.app.repository.entity.User;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(prefix = "m_")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRecordsDTO {

    private long m_userRecordsId;

    private User m_user;

    private Set<ProductRegisterRecord> m_productRegisterRecords;

    private Set<InputRecord> m_inputRecords;

    private Set<OutputRecord> m_outputRecords;
}
