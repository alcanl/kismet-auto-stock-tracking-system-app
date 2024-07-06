package com.alcanl.app.repository.dal;

import com.alcanl.app.repository.*;
import com.alcanl.app.repository.entity.InputRecord;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
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

    public Optional<Stock > findProductStock(Product product)
    {
        try {
            return m_stockRepository.findByProduct(product);
        } catch (Throwable ex) {
            log.error("Error while finding product stock", ex);
            return Optional.empty();
        }
    }
    public Iterable<InputRecord> findInputRecordsByUser(User user)
    {
        try {
            return m_inputRecordRepository.findInputRecordsByUserId(user.userId);
        } catch (Throwable ex) {
            log.error("Error while finding input records by user", ex);
            return new ArrayList<>();
        }
    }
    public boolean existByUsernameAndPassword(String username, String password)
    {
        try {
            return m_userRepository.existsByUsernameAndPassword(username, password);
        } catch (Throwable ex) {
            log.error("Error while existing user by username and password", ex);
            return false;
        }
    }
    public boolean existByEmail(String eMail)
    {
        try {
            return m_userRepository.existsByEmail(eMail);
        } catch (Throwable ex) {
            log.error("Error while existing user by user email", ex);
            return false;
        }
    }
    public Iterable<InputRecord> findByUserAndDate(User user, LocalDate start, LocalDate end)
    {
        try {
            return m_inputRecordRepository.findInputRecordsByUserAndDate(user, start, end);
        } catch (Throwable ex) {
            log.error("Error while finding input records by user and date ", ex);
            return new ArrayList<>();
        }
    }
}
