package com.alcanl.app.repository.dal;

import com.alcanl.app.repository.*;
import com.alcanl.app.repository.entity.*;
import com.alcanl.app.repository.exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class RepositoryDataHelper {
    private final IInputRecordRepository m_inputRecordRepository;
    private final IOutputRecordRepository m_outputRecordRepository;
    private final IStockRepository m_stockRepository;
    private final IProductRepository m_productRepository;
    private final IUserRepository m_userRepository;

    public Optional<Stock > findProductStock(Product product)
    {
        try {
            return m_stockRepository.findByProduct(product);
        } catch (Throwable ex) {
            log.error("Error while finding product stock: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<InputRecord> findInputRecordsByUser(User user)
    {
        try {
            return m_inputRecordRepository.findInputRecordsByUserId(user.userId);
        } catch (Throwable ex) {
            log.error("Error while finding input records by user: {}", ex.getMessage(), ex);
            throw new RepositoryException(ex);
        }
    }
    public boolean existByUsernameAndPassword(String username, String password)
    {
        try {
            return m_userRepository.existsByUsernameAndPassword(username, password);
        } catch (Throwable ex) {
            log.error("Error while existing user by username and password: {}", ex.getMessage());
            return false;
        }
    }
    public Optional<User> findUserByUsernameAndPassword(String username, String password)
    {
        try {
            return m_userRepository.findByUsernameAndPassword(username, password);
        } catch (Throwable ex) {
            log.error("Error while finding user by username and password: {}", ex.getMessage());
            return Optional.empty();
        }
    }
    public Optional<User> findUserByUsername(String username)
    {
        try {
            return m_userRepository.findByUsername(username);
        } catch (Throwable ex) {
            log.error("Error while finding user by username: {}", ex.getMessage());
            return Optional.empty();
        }
    }
    public boolean isUserExistByUsername(String username)
    {
        try {
            return m_userRepository.existsByUsername(username);
        } catch (Throwable ex) {
            log.error("Error while existing user by user email: {}", ex.getMessage());
            return false;
        }
    }
    public Iterable<InputRecord> findInputRecordsByUserAndDate(User user, LocalDate start, LocalDate end)
    {
        try {
            return m_inputRecordRepository.findInputRecordsByUserAndDate(
                    start.getDayOfMonth(), start.getMonthValue(), start.getYear(),
                    end.getDayOfMonth(), end.getMonthValue(), end.getYear(), user.userId);

        } catch (Throwable ex) {
            log.error("Error while finding input records by user and date: {} ", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Optional<Product> findProductById(String id)
    {
        try {
            return m_productRepository.findById(id);
        } catch (Throwable ex) {
            log.error("Error while finding product by id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<Product> findAllProducts()
    {
        try {
            return m_productRepository.findAll();
        } catch (Throwable ex) {
            log.error("Error while finding all products: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<User> findAllUsers()
    {
        try {
            return m_userRepository.findAll();
        } catch (Throwable ex) {
            log.error("Error while finding all users: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Optional<Product> findProductByName(String productName)
    {
        try {
            return m_productRepository.findByProductName(productName);
        } catch (Throwable ex) {
            log.error("Error while finding product by name: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public Iterable<Stock> findAllStockByLesserThan(int lesser)
    {
        try {
            return m_stockRepository.findAllByLesserOrEqualsThan(lesser);
        } catch (Throwable ex) {
            log.error("Error while finding stock by lesser: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public Iterable<Stock> findAllStockByLesserThanThreshold()
    {
        try {
            return m_stockRepository.findAllByLesserThanThreshold();
        } catch (Throwable ex) {
            log.error("Error while finding stock by lesser than threshold: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public Iterable<Stock> findAllStockByGreaterThan(int greater)
    {
        try {
            return m_stockRepository.findAllByGreaterThan(greater);
        } catch (Throwable ex) {
            log.error("Error while finding stock by greater: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Optional<Stock> findStockById(long id)
    {
        try {
            return m_stockRepository.findById(id);
        } catch (Throwable ex) {
            log.error("Error while finding stock by id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public void saveUser(User user)
    {
        try {
            m_userRepository.save(user);

        } catch (Throwable ex) {
            log.error("Error while saving user: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public void deleteUser(User user)
    {
        try {
            m_userRepository.delete(user);
        } catch (Throwable ex) {
            log.error("Error while deleting user: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public Product saveProduct(Product product)
    {
        try {
            return m_productRepository.save(product);
        } catch (Throwable ex) {
            log.error("Error while saving product: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public boolean existProductById(String originalCode)
    {
        try {
            return m_productRepository.existsById(originalCode);
        } catch (Throwable ex) {
            log.error("Error while existing product by id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public void deleteProduct(Product product)
    {
        try {
            m_productRepository.delete(product);
        } catch (Throwable ex) {
            log.error("Error while deleting product: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public Stock saveStock(Stock stock)
    {
        try {
            return m_stockRepository.save(stock);
        } catch (Throwable ex) {
            log.error("Error while saving stock: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public void saveInputRecord(InputRecord inputRecord)
    {
        try {
            m_inputRecordRepository.save(inputRecord);
        } catch (Throwable ex) {
            log.error("Error while saving input record: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    @Transactional
    public void saveOutputRecord(OutputRecord outputRecord)
    {
        try {
            m_outputRecordRepository.save(outputRecord);
        } catch (Throwable ex) {
            log.error("Error while saving output record: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
}
