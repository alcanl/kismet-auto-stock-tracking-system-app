package com.alcanl.app.service.mapper;

import com.alcanl.app.repository.entity.User;
import com.alcanl.app.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "UserMapperImpl", componentModel = "spring")
public interface IUserMapper {

    User userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);
}
