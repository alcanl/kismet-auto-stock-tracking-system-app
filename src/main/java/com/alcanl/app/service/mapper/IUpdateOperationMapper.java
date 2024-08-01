package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.UpdateOperation;
import com.alcanl.app.service.dto.UpdateOperationDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "UpdateOperationMapperImpl", componentModel = "spring")
public interface IUpdateOperationMapper {

    UpdateOperation updateOperationDTOToUpdateOperation(UpdateOperationDTO updateOperationDTO);

    UpdateOperationDTO updateOperationToUpdateOperationDTO(UpdateOperation updateOperation);
}
