package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Stock;
import com.alcanl.app.repository.entity.StockMovement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IStockMovementRepository extends CrudRepository<StockMovement, Long> {

    Iterable<StockMovement> findAllByStockIs(Stock stock);

    @Modifying
    @Query(value = "DELETE FROM stock_movement_info WHERE stock_id = :id ", nativeQuery = true)
    void deleteByStockId(Long id);
}
