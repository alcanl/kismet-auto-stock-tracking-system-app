package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.service.mapper.IOutputRecordMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OutputRecordService {
    private final IOutputRecordMapper m_outputRecordMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;
}
