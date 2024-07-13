package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.OutputRecord;
import com.alcanl.app.service.dto.OutputRecordDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "OutputRecordMapperImpl", componentModel = "spring")
public interface IOutputRecordMapper {
    OutputRecord outputRecordDTOToOutputRecord(OutputRecordDTO outputRecordDTO);

    OutputRecordDTO outputRecordToOutputRecordDTO(OutputRecord outputRecord);
}
