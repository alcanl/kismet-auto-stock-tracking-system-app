package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.*;
import com.alcanl.app.service.dto.*;
import org.mapstruct.Mapper;

@Mapper(implementationName = "ModelMapperImpl", componentModel = "spring")
public interface IModelMapper {

    User userDTOToUser(UserDTO userDTO);
    UserDTO userToUserDTO(User user);
    Product productDTOToProduct(ProductDTO productDTO);
    ProductDTO productToProductDTO(Product product);
    Stock stockDTOToStock(StockDTO stockDTO);
    StockDTO stockToStockDTO(Stock stock);
    InputRecord inputRecordDTOToInputRecord(InputRecordDTO inputRecordDTO);
    InputRecordDTO inputRecordToInputRecordDTO(InputRecord inputRecord);
    OutputRecord outputRecordDTOToOutputRecord(OutputRecordDTO outputRecordDTO);
    OutputRecordDTO outputRecordToOutputRecordDTO(OutputRecord outputRecord);
}
