package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.OutputRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IOutputRecordRepository extends CrudRepository<OutputRecord, Long> {
    @Query("FROM OutputRecord or WHERE or.user.userId = :userId")
    Iterable<OutputRecord> findOutputRecordsByUserId(long userId);

    @Query("FROM OutputRecord or WHERE or.stock.product = :productId")
    Iterable<OutputRecord> findOutputRecordsByProductId(String productId);

    @Query("FROM OutputRecord or WHERE or.user.userId = :userId AND or.stock.product.originalCode = :productId")
    Iterable<OutputRecord> findOutputRecordsByUserAndProductId(Long userId, String productId);

    @Query(value = "select * from stock_output_record_info where original_code = :productId and record_date between (date_part('day', record_date) = :startDay and date_part('month', record_date) = :startMonth and date_part('year', record_date) = :startYear) and (date_part('day', record_date) = :endDay and date_part('month', record_date) = :endMonth and date_part('year', record_date) = :endYear)", nativeQuery = true)
    Iterable<OutputRecord> findOutputRecordsByDateAndProduct(int startDay, int startMonth, int startYear,
                                                           int endDay, int endMonth, int endYear, String productId);

    @Query(value = "select * from stock_output_record_info where user_id = :userId and record_date between (date_part('day', record_date) = :startDay and date_part('month', record_date) = :startMonth and date_part('year', record_date) = :startYear) and (date_part('day', record_date) = :endDay and date_part('month', record_date) = :endMonth and date_part('year', record_date) = :endYear)", nativeQuery = true)
    Iterable<OutputRecord> findOutputRecordsByUserAndDate(int startDay, int startMonth, int startYear,
                                                        int endDay, int endMonth, int endYear, long userId);
}
