package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.entity.StockMovement;
import com.alcanl.app.repository.entity.type.StockMovementType;
import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import com.alcanl.app.service.dto.StockMovementDTO;
import com.alcanl.app.service.dto.UserDTO;
import com.alcanl.app.service.mapper.IProductMapper;
import com.alcanl.app.service.mapper.IStockMapper;
import com.alcanl.app.service.mapper.IStockMovementMapper;
import com.alcanl.app.service.mapper.IUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Slf4j
class StockMovementService {
    private final RepositoryDataHelper m_repositoryDataHelper;
    private final IStockMovementMapper m_stockMovementMapper;
    private final IProductMapper m_productMapper;
    private final IUserMapper m_userMapper;
    private final IStockMapper m_stockMapper;

    @Transactional
    public StockMovement saveNewStockMovement(StockMovementDTO stockMovementDTO, UserDTO userDTO, StockDTO stockDTO, ProductDTO productDTO)
    {
        try {
            var product = m_productMapper.productDTOToProduct(productDTO);
            if (m_repositoryDataHelper.existProductById(product.getOriginalCode()))
                throw new ProductAlreadyExistException();

            var stock = m_stockMapper.stockDTOToStock(stockDTO);
            var user = m_userMapper.userDTOToUser(userDTO);
            var stockMovement = m_stockMovementMapper.stockMovementDTOToStockMovement(stockMovementDTO);
            stock.setProduct(product);
            stockMovement.setUser(user);
            stockMovement.setStock(stock);
            stockMovement.setAmount(stock.getAmount());
            stockMovement.setStockMovementType(StockMovementType.STOCK_REGISTER);

            return m_repositoryDataHelper.saveStockMovement(stockMovement);
        } catch (RepositoryException ex) {
            log.error("Error while saving stock movement {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
}
