package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStockRepository extends CrudRepository<Stock, Long> {
    Optional<Stock> findByProduct(Product product);
}
