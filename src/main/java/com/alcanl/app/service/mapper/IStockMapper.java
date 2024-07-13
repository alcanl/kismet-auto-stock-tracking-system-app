package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.service.dto.StockDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "StockMapperImpl", componentModel = "spring")
public interface IStockMapper {

    Stock stockDTOToStock(StockDTO stockDTO);

    StockDTO stockToStockDTO(Stock stock);
}
