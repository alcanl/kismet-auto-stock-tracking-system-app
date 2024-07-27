package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.service.mapper.IStockMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class StockService {
    private final IStockMapper m_stockMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;
}
