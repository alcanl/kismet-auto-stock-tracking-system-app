package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.OutputRecord;
import com.alcanl.app.repository.entity.Product;
import com.alcanl.app.repository.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface IOutputRecordRepository extends CrudRepository<OutputRecord, Long> {
    Iterable<OutputRecord> findOutputRecordsByUser(User user);
    Iterable<OutputRecord> findOutputRecordsByProduct(Product product);
    Iterable<OutputRecord> findOutputRecordsByUserIdAndProduct(User user, Product product);
    Iterable<OutputRecord> findOutputRecordsByDateAndProduct(LocalDate start, LocalDate end, Product product);
    Iterable<OutputRecord> findOutputRecordsByUserAndDate(User user, LocalDate start, LocalDate end);
}
