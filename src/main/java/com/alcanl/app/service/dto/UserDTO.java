package com.alcanl.app.service.dto;

import com.alcanl.app.repository.entity.InputRecord;
import com.alcanl.app.repository.entity.OutputRecord;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(prefix = "m_")
public class UserDTO {
    private long m_userId;

    @EqualsAndHashCode.Include
    private String m_username;

    @EqualsAndHashCode.Include
    private String m_eMail;

    private String m_firstName;

    @EqualsAndHashCode.Include
    private String m_password;

    private String m_lastName;

    private boolean m_isAdmin;

    private LocalDate m_dateOfRegister;

    private String m_description;

    private Set<InputRecord> m_inputRecord;

    private Set<OutputRecord> m_outputRecord;

}
