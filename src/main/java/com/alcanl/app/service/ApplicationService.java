package com.alcanl.app.service;

import com.alcanl.app.application.ui.event.UpdateTablesEvent;
import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.StockMovement;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
import com.alcanl.app.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CurrentUserConfig m_currentUser;
    private final ExecutorService m_threadPool;
    private final ApplicationEventPublisher m_applicationEventPublisher;

    @Transactional
    public Product saveProduct(ProductDTO productDTO, StockDTO stockDTO) throws ExecutionException, InterruptedException
    {
        return m_threadPool.submit(() -> m_productService.saveProduct(productDTO, stockDTO,
                m_currentUser.getUser())).get();
    }
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

    public void deleteProduct(String productId)
    {
        try {
            m_threadPool.submit(() -> m_productService.deleteProductById(productId)).get();
        } catch (InterruptedException | RuntimeException | ExecutionException ex){
            log.error("Error while deleting product {}, {}", productId, ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }

        m_applicationEventPublisher.publishEvent(new UpdateTablesEvent(this));
    }

    public StockMovement saveNewStockMovement(StockMovementDTO stockMovementDTO, StockDTO stockDTO, ProductDTO productDTO) throws ExecutionException, InterruptedException {
        return m_threadPool.submit(() -> m_stockMovementService.saveNewStockMovement(
                stockMovementDTO, m_currentUser.getUser(), stockDTO, productDTO)).get();
    }
}
