package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends CrudRepository<Product, String> {
}
