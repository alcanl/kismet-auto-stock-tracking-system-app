package com.alcanl.app.service;

import com.alcanl.app.configuration.CurrentUserConfig;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import com.alcanl.app.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Component
@AllArgsConstructor
public class ApplicationService {
    private final UserService m_userService;
    private final StockService m_stockService;
    private final ProductService m_productService;
    private final InputRecordService m_inputRecordService;
    private final OutputRecordService m_outputRecordService;
    private final CurrentUserConfig m_currentUser;
    private final ExecutorService m_threadPool;

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
    public boolean isUserExist(String username, String password)
    {
        return m_userService.isUserExist(username, password);
    }
    public Optional<UserDTO> findUserByUsernameAndPassword(String username, String password)
    {
        return m_userService.findUserByUsernameAndPassword(username, password);
    }
    public List<ProductDTO> getAllStockOutProducts()
    {
        return m_productService.getAllStockOutProducts();
    }
    public List<Product> getAllLesserThanThresholdStockProducts()
    {
        return m_productService.getAllLesserThanThresholdStockProducts();
    }
}
