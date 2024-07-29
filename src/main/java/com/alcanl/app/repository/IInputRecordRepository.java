package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.InputRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IInputRecordRepository extends CrudRepository<InputRecord, Long> {

    @Query("FROM InputRecord ir WHERE ir.userRecords.user.userId = :userId")
    Iterable<InputRecord> findInputRecordsByUserId(long userId);

    @Query("FROM InputRecord ir WHERE ir.productInput.originalCode = :productId")
    Iterable<InputRecord> findInputRecordsByProductId(String productId);

    @Query("FROM InputRecord ir WHERE ir.userRecords.user.userId = :userId AND ir.productInput.originalCode = :productId")
    Iterable<InputRecord> findInputRecordsByUserAndProductId(Long userId, String productId);

}

