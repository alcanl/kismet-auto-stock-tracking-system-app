package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.mapper.IStockMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
class StockService {
    private final IStockMapper m_stockMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;

    public Stock saveStock(Stock stock)
    {
        try {
            return m_repositoryDataHelper.saveStock(stock);
        }
        catch (RepositoryException ex) {
            log.error("error while saving Stock : {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
}
