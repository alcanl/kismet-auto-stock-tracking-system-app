package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.exception.ProductAlreadyExistException;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.dto.StockDTO;
import com.alcanl.app.service.dto.UserDTO;
import com.alcanl.app.service.mapper.IProductMapper;
import com.alcanl.app.service.mapper.IStockMapper;
import com.alcanl.app.service.mapper.IUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
class ProductService {
    private final IProductMapper m_productMapper;
    private final IStockMapper m_stockMapper;
    private final IUserMapper m_userMapper;
    private final RepositoryDataHelper m_repositoryDataHelper;

    public List<ProductDTO> getAllStockOutProducts()
    {
        var list = new ArrayList<ProductDTO>();
        try {
            m_repositoryDataHelper.findAllStockByLesserThan(0).forEach(s -> list.add(m_productMapper.productToProductDTO(s.product)));

        } catch (RepositoryException ex) {
            log.error("ProductService::getAllStockOutProducts: {}", ex.getMessage());
        }
        return list;
    }
    public List<ProductDTO> getAllLesserThanThresholdStockProducts()
    {
        try {
            var list = new ArrayList<ProductDTO>();
            StreamSupport.stream(m_repositoryDataHelper.findAllStockByLesserThanThreshold().spliterator(), false)
                            .filter(s -> s.amount != 0)
                    .forEach(s -> list.add(m_productMapper.productToProductDTO(s.product)));

            return list;
        } catch (RepositoryException ex) {
            log.error("ProductService::getAllStockLesserThanThresholdProducts: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    public Product saveProduct(ProductDTO productDTO, StockDTO stockDTO, UserDTO userDTO)
    {
        try {
            if (m_repositoryDataHelper.existProductById(productDTO.getOriginalCode()))
                throw new ProductAlreadyExistException(productDTO.getOriginalCode());

            var user = m_userMapper.userDTOToUser(userDTO);
            var stock = m_stockMapper.stockDTOToStock(stockDTO);
            productDTO.setStock(stock);
            //productDTO.setRecorderUser(user);
            return m_repositoryDataHelper.saveProduct(m_productMapper.productDTOToProduct(productDTO));
        }
        catch (RepositoryException ex) {
            log.error("ProductService::saveProduct: {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public Optional<ProductDTO> findProductById(String productId)
    {
        try {
            return m_repositoryDataHelper.findProductById(productId).map(m_productMapper::productToProductDTO);
        } catch (RepositoryException ex) {
            log.error("ProductService::findProductById: {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
    public void deleteProductById(String productId)
    {
        try {
            m_repositoryDataHelper.deleteProduct(productId);
        }catch (RepositoryException ex) {
            log.error("ProductService::deleteProductById : {}", ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }
}
