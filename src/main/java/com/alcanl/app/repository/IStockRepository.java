package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStockRepository extends CrudRepository<Stock, Long> {
    Optional<Stock> findByProduct(Product product);

    @Query("FROM Stock s WHERE s.amount <= :lesser")
    Iterable<Stock> findAllByLesserOrEqualsThan(int lesser);

    @Query("FROM Stock s WHERE s.amount < s.threshold")
    Iterable<Stock> findAllByLesserThanThreshold();

    @Query("FROM Stock s WHERE s.amount >= :greater")
    Iterable<Stock> findAllByGreaterThan(int greater);

}
