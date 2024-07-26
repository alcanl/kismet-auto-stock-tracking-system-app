package com.alcanl.app.service;

import com.alcanl.app.repository.dal.RepositoryDataHelper;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.exception.RepositoryException;
import com.alcanl.app.service.dto.ProductDTO;
import com.alcanl.app.service.mapper.IProductMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {
    private final IProductMapper m_productMapper;
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
    public List<Product> getAllLesserThanThresholdStockProducts()
    {
        try {
            var list = new ArrayList<Product>();
            m_repositoryDataHelper.findAllStockByLesserThan(0).forEach(s -> list.add(s.product));

            return list;
        } catch (RepositoryException ex) {
            log.error("ProductService::getAllStockLesserThanThresholdProducts: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }
}
