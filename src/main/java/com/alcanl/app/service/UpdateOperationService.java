package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.entity.UpdateOperation;
import com.alcanl.app.repository.entity.type.UpdateOperationType;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.*;
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
class UpdateOperationService {
    private final RepositoryDataHelper m_repositoryDataHelper;
    private final IStockMovementMapper m_stockMovementMapper;
    private final IProductMapper m_productMapper;
    private final IUserMapper m_userMapper;
    private final IStockMapper m_stockMapper;

    @Transactional
    public UpdateOperation saveNewUpdateOperation(ProductDTO productDTO, UserDTO userDTO, UpdateOperationType updateOperationType)
    {
        try {
            var updateOperation = new UpdateOperation();
            var user = m_userMapper.userDTOToUser(userDTO);
            productDTO.getStock().setProduct(m_productMapper.productDTOToProduct(productDTO));
            updateOperation.setUser(user);
            updateOperation.setStock(productDTO.getStock());
            updateOperation.setUpdateOperationType(updateOperationType);

            m_repositoryDataHelper.saveStock(productDTO.getStock());
            return m_repositoryDataHelper.saveUpdateOperation(updateOperation);

        }
        catch (RepositoryException ex) {
            log.error("error while saving new updateOperation : {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    @Transactional
    public void deleteUpdateOperationsByProduct(ProductDTO productDTO)
    {
        try {
            m_repositoryDataHelper.deleteUpdateOperationsByStockId(productDTO.getStock().getStockId());
        }catch (RepositoryException ex) {
            log.error("Error while deleting stock movement {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }

    public boolean existsUpdateOperationsByProduct(ProductDTO productDTO)
    {
        try {
            return m_repositoryDataHelper.existsUpdateOperationByStockId(productDTO.getStock().getStockId());
        }catch (RepositoryException ex) {
            log.error("Error while checking update operations {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
}
