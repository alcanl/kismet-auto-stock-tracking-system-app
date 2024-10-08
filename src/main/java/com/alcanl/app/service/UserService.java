package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.UserDTO;
import com.alcanl.app.service.mapper.IUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
class UserService {
    private final IUserMapper m_userMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;
    private final PasswordEncoder m_passwordEncoder;

    @Transactional
    public void saveUser(UserDTO userDTO)
    {
        try {
            userDTO.setPassword(m_passwordEncoder.encode(userDTO.getPassword()));
            m_repositoryDataHelper.saveUser(m_userMapper.userDTOToUser(userDTO));
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }

    @Transactional
    public void deleteUser(UserDTO userDTO)
    {
        try {
            var user = m_repositoryDataHelper.findUserByUsername(userDTO.getUsername());
            user.ifPresent(m_repositoryDataHelper::deleteUser);
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO)
    {
        try {
            var user = m_repositoryDataHelper.findUserByUsername(userDTO.getUsername());
            user.ifPresent(u -> {
                u.setFirstName(userDTO.getFirstName());
                u.setLastName(userDTO.getLastName());
                u.setAdmin(userDTO.isAdmin());
                u.setPassword(m_passwordEncoder.encode(userDTO.getPassword()));
                u.setDescription(userDTO.getDescription());
                u.setEMail(userDTO.getEMail());
                if (u.getEMail().equals(userDTO.getEMail()) && u.getUsername().equals(userDTO.getUsername()))
                    m_repositoryDataHelper.updateUser(u);
                else
                    m_repositoryDataHelper.saveUser(u);
            });

            return m_userMapper.userToUserDTO(user.orElseThrow(() -> new ServiceException("User not found")));
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public Optional<UserDTO> findUserByUsernameAndPassword(String username, String password)
    {
        var userDtoOpt = m_repositoryDataHelper.findUserByUsername(username).map(m_userMapper::userToUserDTO);

        return userDtoOpt.isEmpty() ? Optional.empty() :
                m_passwordEncoder.matches(password, userDtoOpt.get().getPassword()) ? userDtoOpt : Optional.empty();

    }
    public Optional<UserDTO> findUserByUsername(String username)
    {
       return m_repositoryDataHelper.findUserByUsername(username).map(m_userMapper::userToUserDTO);
    }
    public boolean isUserExist(String username)
    {
        try {
            return m_repositoryDataHelper.isUserExistByUsername(username);
        } catch (RepositoryException ex) {
            log.error("Error in UserService.isUserExist : {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<UserDTO> findAllUsers()
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllUsers().spliterator(), false)
                    .map(m_userMapper::userToUserDTO).toList();
        } catch (RepositoryException ex) {
            log.error("Error in UserService.findAllUsers : {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public boolean isOldPasswordTrue(UserDTO currentUser, String oldPassword)
    {
        return m_passwordEncoder.matches( oldPassword, currentUser.getPassword());
    }
}
