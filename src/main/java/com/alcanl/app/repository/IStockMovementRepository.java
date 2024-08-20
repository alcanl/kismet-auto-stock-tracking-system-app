package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.StockMovement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface IStockMovementRepository extends CrudRepository<StockMovement, Long> {

    @Modifying
    @Query(value = "DELETE FROM stock_movement_info WHERE stock_id = :id ", nativeQuery = true)
    void deleteByStockId(Long id);

    boolean existsByStockStockId(Long id);

    @Query("FROM StockMovement sm WHERE sm.stock.product.originalCode = :id")
    Iterable<StockMovement> findAllByProductId(String id);

    @Query("FROM StockMovement sm WHERE sm.user.userId = :id")
    Iterable<StockMovement> findAllByUserId(long id);

    @Query("FROM StockMovement ORDER BY stockMovementId DESC LIMIT 20")
    Iterable<StockMovement> findLastTwentyRecords();

    Iterable<StockMovement> findAllByRecordDateBetween(LocalDate from, LocalDate to);

    Iterable<StockMovement> findAllByUserUserIdAndRecordDateBetween(long id, LocalDate from, LocalDate to);

    Iterable<StockMovement> findAllByStockProductOriginalCodeAndRecordDateBetween(String productId, LocalDate from, LocalDate to);

    Iterable<StockMovement> findAllByStockProductOriginalCodeAndRecordDateBetweenAndUserUserId(String productId, LocalDate from, LocalDate to, Long id);

    Iterable<StockMovement> findAllByStockProductOriginalCodeAndUserUserId(String productId, long id);
}
