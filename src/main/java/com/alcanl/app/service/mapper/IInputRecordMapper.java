package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.*;
import com.alcanl.app.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "InputRecordMapperImpl", componentModel = "spring")
public interface IInputRecordMapper {

    InputRecord inputRecordDTOToInputRecord(InputRecordDTO inputRecordDTO);

    InputRecordDTO inputRecordToInputRecordDTO(InputRecord inputRecord);

}
