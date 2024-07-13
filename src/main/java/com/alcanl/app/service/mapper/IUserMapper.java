package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.User;
import com.alcanl.app.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper {

    @Mapping(source = "EMail", target = "eMail")
    @Mapping(source = "admin", target = "isAdmin")
    User userDTOToUser(UserDTO userDTO);

    @Mapping(source = "eMail", target = "EMail")
    @Mapping(source = "isAdmin", target = "admin")
    UserDTO userToUserDTO(User user);
}
