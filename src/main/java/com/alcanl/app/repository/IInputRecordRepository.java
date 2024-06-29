package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.InputRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInputRecordRepository extends CrudRepository<InputRecord, Long> {
}
