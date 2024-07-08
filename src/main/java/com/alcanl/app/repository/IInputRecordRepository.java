package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.InputRecord;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IInputRecordRepository extends CrudRepository<InputRecord, Long> {

    Iterable<InputRecord> findInputRecordsByUserId(long userId);
    Iterable<InputRecord> findInputRecordsByProduct(Product product);
    Iterable<InputRecord> findInputRecordsByUserAndProduct(User user, Product product);
    Iterable<InputRecord> findInputRecordsByDateAndProduct(LocalDate start, LocalDate end, Product product);
    Iterable<InputRecord> findInputRecordsByUserAndDate(User user, LocalDate start, LocalDate end);
}

