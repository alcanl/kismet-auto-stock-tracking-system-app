package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.StockMovement;
import com.alcanl.app.service.dto.StockMovementDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "StockMovementMapperImpl", componentModel = "spring")
public interface IStockMovementMapper {

    StockMovement stockMovementDTOToStockMovement(StockMovementDTO stockMovementDTO);

    StockMovementDTO stockMovementToStockMovementDTO(StockMovement stockMovement);
}
