package com.alcanl.app.repository.dal;

import com.alcanl.app.repository.*;
import com.alcanl.app.repository.entity.*;
import com.alcanl.app.repository.exception.EmailAlreadyInUseException;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.repository.exception.UsernameAlreadyInUseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class RepositoryDataHelper {
    private final IStockRepository m_stockRepository;
    private final IProductRepository m_productRepository;
    private final IUserRepository m_userRepository;
    private final IStockMovementRepository m_stockMovementRepository;
    private final IUpdateOperationRepository m_updateOperationRepository;

    public Iterable<StockMovement> findAllStockMovementsByProductId(String productId)
    {
        try {
            return m_stockMovementRepository.findAllByProductId(productId);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by product Id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findAllStockMovementsByProductIdAndDateBetween(String productId, LocalDate startDate, LocalDate endDate)
    {
        try {
            return m_stockMovementRepository.findAllByStockProductOriginalCodeAndRecordDateBetween(productId, startDate, endDate);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by product Id And Date Between: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findAllStockMovementsByProductIdAndUserId(String productId, long userId)
    {
        try {
            return m_stockMovementRepository.findAllByStockProductOriginalCodeAndUserUserId(productId, userId);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by product Id and User id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findAllStockMovementsByProductIdAndUserIdAndDateBetween(String productId, long userId, LocalDate startDate, LocalDate endDate)
    {
        try {
            return m_stockMovementRepository.findAllByStockProductOriginalCodeAndRecordDateBetweenAndUserUserId(productId, startDate, endDate, userId);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by product Id and user id and date between: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findAllStockMovementsByUserIdAndDateBetween(long userId, LocalDate startDate, LocalDate endDate)
    {
        try {
            return m_stockMovementRepository.findAllByUserUserIdAndRecordDateBetween(userId, startDate, endDate);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by user id and date between: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findAllStockMovementsByUserId(long userId)
    {
        try {
            return m_stockMovementRepository.findAllByUserId(userId);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by user Id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findAllStockMovementsByDateBetween(LocalDate from, LocalDate to)
    {
        try {
            return m_stockMovementRepository.findAllByRecordDateBetween(from, to);
        }catch (Throwable ex) {
            log.error("Error while finding all StockMovements by date between: {}", ex.getMessage());
            throw new RepositoryException(ex);
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

    public Optional<Product> findProductById(String id)
    {
        try {
            return m_productRepository.findById(id);
        } catch (Throwable ex) {
            log.error("Error while finding product by id: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<Stock> findAllStocks()
    {
        try {
            return m_stockRepository.findAllStocks();
        } catch (Throwable ex) {
            log.error("Error while finding all product: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Optional<Product> findProductByStockCode(String stockCode)
    {
        try {
            return m_productRepository.findByStockCode(stockCode);
        } catch (Throwable ex) {
            log.error("Error while finding product by stock code: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public Iterable<Stock> findAllStockByLesserThan(int lesser)
    {
        try {
            return m_stockRepository.findAllByLesserOrEqualsThan(lesser);
        } catch (Throwable ex) {
            log.error("Error while finding product by lesser: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public Iterable<Stock> findAllStockByLesserThanThreshold()
    {
        try {
            return m_stockRepository.findAllByLesserThanThreshold();
        } catch (Throwable ex) {
            log.error("Error while finding product by lesser than threshold: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<User> findAllUsers()
    {
        try {
            return m_userRepository.findAllUsers();
        } catch (Throwable ex) {
            log.error("Error while finding all users : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<StockMovement> findLastTwentyStockMovementRecord()
    {
        try {
            return m_stockMovementRepository.findLastTwentyRecords();
        } catch (Throwable ex) {
            log.error("Error while finding last twenty stock move records: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public void saveUser(User user)
    {
        try {
            if (m_userRepository.existsUserByeMail(user.getEMail()))
                throw new EmailAlreadyInUseException("Girilen Email Adresi Sistemde Kayıtlı!");

            if (m_userRepository.existsByUsername(user.getUsername()))
                throw new UsernameAlreadyInUseException("Girilen Kullanıcı Adı Sistemde Kayıtlı!");
            m_userRepository.save(user);

        } catch (Throwable ex) {
            log.error("Error while saving user: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public void deleteUser(User user)
    {
        try {
            m_userRepository.delete(user);
        } catch (Throwable ex) {
            log.error("Error while deleting user: {}", ex.getMessage());
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
    public void deleteProduct(String productId)
    {
        try {
            m_productRepository.deleteById(productId);
        } catch (Throwable ex) {
            log.error("Error while deleting product: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Stock saveStock(Stock stock)
    {
        try {
            return m_stockRepository.save(stock);
        } catch (Throwable ex) {
            log.error("Error while saving stock: {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public StockMovement saveStockMovement(StockMovement stockMovement)
    {
        try {
            return m_stockMovementRepository.save(stockMovement);
        } catch (Throwable ex) {
            log.error("Error while saving stock movement {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public Iterable<Product> findAllProductsByContains(String productName)
    {
        try {
            return m_productRepository.findByProductNameContaining(productName);
        } catch (Throwable ex) {
            log.error("Error while finding product by contains {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public void deleteStockById(long id)
    {
        try {
            m_stockRepository.deleteById(id);
        }catch (Throwable ex) {
            log.error("Error while deleting stock id : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public void deleteStockMovementsByStockId(long id)
    {
        try {
            m_stockMovementRepository.deleteByStockId(id);
        } catch (Throwable ex) {
            log.error("Error while deleting stockMovements by stock id : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public void deleteUpdateOperationsByStockId(long id)
    {
        try {
            m_updateOperationRepository.deleteByStockId(id);
        } catch (Throwable ex) {
            log.error("Error while deleting update operations by stock id : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public UpdateOperation saveUpdateOperation(UpdateOperation updateOperation)
    {
        try {
            return m_updateOperationRepository.save(updateOperation);
        }catch (Throwable ex) {
            log.error("Error while saving update operation : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public boolean existsUpdateOperationByStockId(long stockId)
    {
        try {
            return m_updateOperationRepository.existsByStockStockId(stockId);
        }catch (Throwable ex) {
            log.error("Error while checking update operations by stockId : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }
    public boolean existsStockMovementsByStockId(long stockId)
    {
        try {
            return m_stockMovementRepository.existsByStockStockId(stockId);
        }catch (Throwable ex) {
            log.error("Error while checking stock movements by stockId : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

    public Iterable<StockMovement> findAllStockMovements()
    {
        try {
            return m_stockMovementRepository.findAll();
        }catch (Throwable ex) {
            log.error("Error while finding all stock movements : {}", ex.getMessage());
            throw new RepositoryException(ex);
        }
    }

}
