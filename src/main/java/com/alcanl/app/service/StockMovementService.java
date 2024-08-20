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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

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
            var savedStock = m_repositoryDataHelper.saveStock(stock);
            stockMovement.setStock(savedStock);
            stockMovement.setUser(user);
            stockMovement.setAmount(stock.getAmount());
            stockMovement.setStockMovementType(StockMovementType.STOCK_REGISTER);
            return m_repositoryDataHelper.saveStockMovement(stockMovement);
        } catch (RepositoryException ex) {
            log.error("Error while saving stock movement {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    @Transactional
    public StockMovement saveNewStockMovementWithUpdate(StockMovementDTO stockMovementDTO, UserDTO userDTO, ProductDTO productDTO)
    {
        try {
            var product = m_productMapper.productDTOToProduct(productDTO);
            var user = m_userMapper.userDTOToUser(userDTO);
            stockMovementDTO.setUser(user);
            stockMovementDTO.getStock().setProduct(product);
            var stockMovement = m_stockMovementMapper.stockMovementDTOToStockMovement(stockMovementDTO);
            return m_repositoryDataHelper.saveStockMovement(stockMovement);
        } catch (RepositoryException ex) {
            log.error("Error while saving stockMovement with update {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    @Transactional
    public void deleteStockMovementsByProduct(ProductDTO productDTO)
    {
        try {
            m_repositoryDataHelper.deleteStockMovementsByStockId(productDTO.getStock().getStockId());
        }catch (RepositoryException ex) {
            log.error("Error while deleting stock movement {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }

    public boolean existsStockMovementsByProduct(ProductDTO productDTO)
    {
        try {
            return m_repositoryDataHelper.existsStockMovementsByStockId(productDTO.getStock().getStockId());
        }catch (RepositoryException ex) {
            log.error("Error while checking stock movements {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByProductId(String productId)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByProductId(productId)
                    .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO)
                    .toList();
        } catch (RepositoryException ex) {
            log.error("Error while finding stock movement by product {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findLastTwentyRecords()
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findLastTwentyStockMovementRecord()
                            .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO)
                    .toList();
        } catch (RepositoryException ex) {
            log.error("Error while finding last stock movement records {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByDateBetween(LocalDate startDate, LocalDate endDate)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByDateBetween(
                    startDate, endDate).spliterator(), false)
                    .map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        }catch (RepositoryException ex) {
            log.error("Error while finding stock movement by date between {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByUserId(long userId)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByUserId(userId)
                    .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        }catch (RepositoryException ex) {
            log.error("Error while finding stock movement by user {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByUserIdAndProductId(long userId, String productId)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByProductIdAndUserId(productId, userId)
                    .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        }catch (RepositoryException ex) {
            log.error("Error while finding stock movements by user and product {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByUserIdAndDateBetween(long userId, LocalDate startDate, LocalDate endDate)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByUserIdAndDateBetween(userId, startDate, endDate)
                    .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        }catch (RepositoryException ex) {
            log.error("Error while finding stock movements by user and date between {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByUserIdAndProductIdAndDateBetween(long userId, String productId, LocalDate startDate, LocalDate endDate)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByProductIdAndUserIdAndDateBetween(productId, userId, startDate, endDate)
                    .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        }catch (RepositoryException ex) {
            log.error("Error while finding stock movement by all filters {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllByProductIdAndDateBetween(String productId, LocalDate startDate, LocalDate endDate)
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovementsByProductIdAndDateBetween(productId, startDate, endDate)
                    .spliterator(), false).map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        }catch (RepositoryException ex) {
            log.error("Error while finding stock movements by product and date between {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public List<StockMovementDTO> findAllStockMovements()
    {
        try {
            return StreamSupport.stream(m_repositoryDataHelper.findAllStockMovements().spliterator(), false)
                    .map(m_stockMovementMapper::stockMovementToStockMovementDTO).toList();
        } catch (RepositoryException ex) {
            log.error("Error while finding all stock movements in service : {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
}
