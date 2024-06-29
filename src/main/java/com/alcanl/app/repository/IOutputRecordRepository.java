package com.alcanl.app.repository;

import com.alcanl.app.repository.entity.OutputRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOutputRecordRepository extends CrudRepository<OutputRecord, Long> {
}
