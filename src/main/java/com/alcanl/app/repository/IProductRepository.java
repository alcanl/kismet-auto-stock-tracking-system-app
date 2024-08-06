package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product, String> {
    Optional<Product> findByProductName(String productName);
    Iterable<Product> findByProductNameContaining(String productName);
    Optional<Product> findByStockCode(String stockCode);
}
