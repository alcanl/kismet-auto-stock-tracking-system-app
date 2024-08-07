package com.alcanl.app.service;

import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.repository.entity.StockMovement;
import com.alcanl.app.repository.entity.UpdateOperation;
import com.alcanl.app.repository.entity.type.UpdateOperationType;
import com.alcanl.app.service.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationService {
    private final UserService m_userService;
    private final StockService m_stockService;
    private final StockMovementService m_stockMovementService;
    private final ProductService m_productService;
    private final UpdateOperationService m_updateOperationService;
    private final CurrentUserConfig m_currentUser;
    private final ExecutorService m_threadPool;
    private final ApplicationEventPublisher m_applicationEventPublisher;

    @Transactional
    public void saveUser(UserDTO userDTO)
    {
        m_userService.saveUser(userDTO);
    }
    public boolean isUserExist(String username)
    {
        return m_userService.isUserExist(username);
    }
    public Optional<UserDTO> findUserByUsernameAndPassword(String username, String password)
    {
        return m_userService.findUserByUsernameAndPassword(username, password);
    }

    public List<ProductDTO> getAllStockOutProducts()
    {
        return m_productService.getAllStockOutProducts();
    }
    public List<ProductDTO> getAllLesserThanThresholdStockProducts()
    {
        return m_productService.getAllLesserThanThresholdStockProducts();
    }

    public Optional<ProductDTO> findProductById(String productId)
    {
        return m_productService.findProductById(productId);
    }

    public Optional<ProductDTO> findProductByStockCode(String stockCode)
    {
        return m_productService.findProductByStockCode(stockCode);
    }

    public void deleteProduct(ProductDTO productDTO)
    {
        try {
            m_threadPool.submit(() -> {
                if (m_stockMovementService.existsStockMovementsByProduct(productDTO))
                    m_stockMovementService.deleteStockMovementsByProduct(productDTO);

                if (m_updateOperationService.existsUpdateOperationsByProduct(productDTO))
                    m_updateOperationService.deleteUpdateOperationsByProduct(productDTO);

                m_productService.deleteProductById(productDTO.getOriginalCode());
            }).get();

        } catch (InterruptedException | RuntimeException | ExecutionException ex){
            log.error("Error while deleting product {}, {}", productDTO.getOriginalCode(), ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }

        m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
    }

    public StockMovement saveNewStockMovement(StockMovementDTO stockMovementDTO, StockDTO stockDTO, ProductDTO productDTO) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() -> m_stockMovementService.saveNewStockMovement(
                stockMovementDTO, m_currentUser.getUser(), stockDTO, productDTO)).get();
    }
    public StockMovement saveNewStockMovementWithUpdateItem(StockMovementDTO stockMovementDTO, ProductDTO productDTO) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() -> {
                    m_stockService.saveStock(stockMovementDTO.getStock());
                    return m_stockMovementService.saveNewStockMovementWithUpdate(
                            stockMovementDTO, m_currentUser.getUser(), productDTO);
                }).get();
    }
    public List<ProductDTO> findAllProductsByContains(String productName) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() -> m_productService.findAllProductsByContains(productName)).get();
    }

    public UpdateOperation updateProduct(ProductDTO productDTO, UpdateOperationType updateOperationType) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() -> m_updateOperationService.saveNewUpdateOperation(
                productDTO, m_currentUser.getUser(), updateOperationType)).get();
    }
    public List<StockMovementDTO> findAllStockMovementsByUser(String username) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() -> {
            var user = m_userService.findUserByUsername(username);
            return user.isPresent() ? m_stockMovementService.findAllByUserId(user.get().getUserId()) : new ArrayList<StockMovementDTO>();
        }).get();
    }
    public List<StockMovementDTO> findAllStockMovementsByProduct(String originalCode) throws ExecutionException, InterruptedException {
        return m_threadPool.submit(() ->
                m_stockMovementService.findAllByProductId(originalCode)).get();
    }
    public List<StockMovementDTO> findAllStockMovementsByDateBetween(LocalDate startDate, LocalDate endDate) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() ->
                m_stockMovementService.findAllByDateBetween(startDate, endDate)).get();
    }
    public List<StockMovementDTO> findAllStockMovementsByProductAndDate(LocalDate startDate, LocalDate endDate, String originalCode) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() ->
                m_stockMovementService.findAllByProductIdAndDateBetween(originalCode, startDate, endDate)).get();
    }
    public List<StockMovementDTO> findAllStockMovementsByProductAndUser(String userName, String originalCode) throws ExecutionException, InterruptedException {
        var user = m_userService.findUserByUsername(userName);
        return user.isPresent() ? m_threadPool.submit(() ->
                m_stockMovementService.findAllByUserIdAndProductId(user.get().getUserId(), originalCode)).get()
                : new ArrayList<>();
    }
    public List<StockMovementDTO> findAllStockMovementsByUserAndDate(String userName, LocalDate startDate, LocalDate endDate) throws ExecutionException, InterruptedException {
        var user = m_userService.findUserByUsername(userName);
        return user.isPresent() ? m_threadPool.submit(() ->
                m_stockMovementService.findAllByUserIdAndDateBetween(user.get().getUserId(), startDate, endDate)).get()
                : new ArrayList<>();
    }
    public List<StockMovementDTO> findAllStockMovementsByAllCriteria(String userName, String originalCode, LocalDate startDate, LocalDate endDate) throws ExecutionException, InterruptedException
    {
         var user = m_userService.findUserByUsername(userName);
         return user.isPresent() ? m_threadPool.submit(() ->
                 m_stockMovementService.findAllByUserIdAndProductIdAndDateBetween(
                         user.get().getUserId(), originalCode, startDate, endDate)).get() : new ArrayList<>();
    }
    public List<StockMovementDTO> findAllStockMovements() throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(m_stockMovementService::findAllStockMovements).get();
    }
}
