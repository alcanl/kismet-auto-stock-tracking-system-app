package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.UpdateOperation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IUpdateOperationRepository extends CrudRepository<UpdateOperation, Long> {

    @Modifying
    @Query(value = "DELETE FROM update_operation_info WHERE stock_id = :id ", nativeQuery = true)
    void deleteByStockId(Long id);

    boolean existsByStockStockId(Long id);
}
