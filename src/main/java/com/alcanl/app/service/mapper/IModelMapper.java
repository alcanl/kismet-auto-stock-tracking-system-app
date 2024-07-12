package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.*;
import com.alcanl.app.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "ModelMapperImpl", componentModel = "spring")
public interface IModelMapper {

    @Mapping(source = "EMail", target = "eMail")
    @Mapping(source = "admin", target = "isAdmin")
    User userDTOToUser(UserDTO userDTO);

    @Mapping(source = "eMail", target = "EMail")
    @Mapping(source = "isAdmin", target = "admin")
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
