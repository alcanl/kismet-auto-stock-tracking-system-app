package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.StockMovement;
import org.springframework.data.repository.CrudRepository;

public interface IStockMovementRepository extends CrudRepository<StockMovement, Long> {
}
