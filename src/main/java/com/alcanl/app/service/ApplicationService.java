package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.service.mapper.IModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final IModelMapper m_mapper;
    private final RepositoryDataHelper m_repositoryDataHelper;
}
