package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockRepository extends CrudRepository<Stock, Long> {

    @Query(value = "SELECT * FROM stock_info", nativeQuery = true)
    Iterable<Stock> findAllStocks();

    @Query("FROM Stock s WHERE s.amount <= :lesser")
    Iterable<Stock> findAllByLesserOrEqualsThan(int lesser);

    @Query("FROM Stock s WHERE s.amount < s.threshold")
    Iterable<Stock> findAllByLesserThanThreshold();

}
