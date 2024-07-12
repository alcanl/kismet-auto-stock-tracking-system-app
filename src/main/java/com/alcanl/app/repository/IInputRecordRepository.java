package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.InputRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IInputRecordRepository extends CrudRepository<InputRecord, Long> {

    @Query("FROM InputRecord ir WHERE ir.user.userId = :userId")
    Iterable<InputRecord> findInputRecordsByUserId(long userId);

    @Query("FROM InputRecord ir WHERE ir.stock.product = :productId")
    Iterable<InputRecord> findInputRecordsByProductId(String productId);

    @Query("FROM InputRecord ir WHERE ir.user.userId = :userId AND ir.stock.product.originalCode = :productId")
    Iterable<InputRecord> findInputRecordsByUserAndProductId(Long userId, String productId);

    @Query(value = "select * from stock_input_record_info siri where original_code = :productId and record_date between (date_part('day', record_date) = :startDay and date_part('month', record_date) = :startMonth and date_part('year', record_date) = :startYear) and (date_part('day', record_date) = :endDay and date_part('month', record_date) = :endMonth and date_part('year', record_date) = :endYear)", nativeQuery = true)
    Iterable<InputRecord> findInputRecordsByDateAndProduct(int startDay, int startMonth, int startYear,
                                                           int endDay, int endMonth, int endYear, String productId);

    @Query(value = "select * from stock_input_record_info siri where user_id = :userId and record_date between (date_part('day', record_date) = :startDay and date_part('month', record_date) = :startMonth and date_part('year', record_date) = :startYear) and (date_part('day', record_date) = :endDay and date_part('month', record_date) = :endMonth and date_part('year', record_date) = :endYear)", nativeQuery = true)
    Iterable<InputRecord> findInputRecordsByUserAndDate(int startDay, int startMonth, int startYear,
                                                        int endDay, int endMonth, int endYear, long userId);
}

