package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.UserDTO;
import com.alcanl.app.service.mapper.IInputRecordMapper;
import com.alcanl.app.service.mapper.IUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final IUserMapper m_userMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;

    public void saveAdmin(UserDTO userDTO)
    {
        try {
            m_repositoryDataHelper.saveUser(m_userMapper.userDTOToUser(userDTO));
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
        }
    }
    public boolean isUserExist(String username, String password)
    {
        return m_repositoryDataHelper.existByUsernameAndPassword(username, password);
    }
}
