package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.OutputRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IOutputRecordRepository extends CrudRepository<OutputRecord, Long> {
    @Query("FROM OutputRecord or WHERE or.userRecords.user.userId = :userId")
    Iterable<OutputRecord> findOutputRecordsByUserId(long userId);

    @Query("FROM OutputRecord or WHERE or.productOutput.originalCode = :productId")
    Iterable<OutputRecord> findOutputRecordsByProductId(String productId);

    @Query("FROM OutputRecord or WHERE or.userRecords.user.userId = :userId AND or.productOutput.originalCode = :productId")
    Iterable<OutputRecord> findOutputRecordsByUserAndProductId(Long userId, String productId);

}
