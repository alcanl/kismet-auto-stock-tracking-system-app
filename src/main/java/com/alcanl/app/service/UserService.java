package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.UserDTO;
import com.alcanl.app.service.mapper.IUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final IUserMapper m_userMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;
    private final PasswordEncoder m_passwordEncoder;

    @Transactional
    public void saveUser(UserDTO userDTO)
    {
        try {
            m_repositoryDataHelper.saveUser(m_userMapper.userDTOToUser(userDTO));
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
        }
    }
    public boolean isUserExist(String username, String password)
    {
        var userDtoOpt = m_repositoryDataHelper.findUserByUsername(username).map(m_userMapper::userToUserDTO);

        return userDtoOpt.filter(userDTO -> m_passwordEncoder.matches(password, userDTO.getPassword())).isPresent();
    }
    public Optional<UserDTO> findUserByUsernameAndPassword(String username, String password)
    {
        var userDtoOpt = m_repositoryDataHelper.findUserByUsername(username).map(m_userMapper::userToUserDTO);

        return userDtoOpt.isEmpty() ? Optional.empty() :
                m_passwordEncoder.matches(password, userDtoOpt.get().getPassword()) ? userDtoOpt : Optional.empty();

    }
}
