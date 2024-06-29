package com.alcanl.app.repository.dal;

import com.alcanl.app.repository.*;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDataHelper {
    private final IInputRecordRepository m_inputRecordRepository;
    private final IOutputRecordRepository m_outputRecordRepository;
    private final IStockRepository m_stockRepository;
    private final IProductRepository m_productRepository;
    private final IUserRepository m_userRepository;

    public RepositoryDataHelper(IInputRecordRepository inputRecordRepository,
                                IOutputRecordRepository outputRecordRepository,
                                IStockRepository stockRepository,
                                IProductRepository productRepository,
                                IUserRepository userRepository)
    {
        m_inputRecordRepository = inputRecordRepository;
        m_outputRecordRepository = outputRecordRepository;
        m_stockRepository = stockRepository;
        m_productRepository = productRepository;
        m_userRepository = userRepository;
    }
}
