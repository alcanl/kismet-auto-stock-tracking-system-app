package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product, String> {
    Iterable<Product> findByProductNameContainingIgnoreCase(String productName);
    Optional<Product> findByStockCode(String stockCode);
}
