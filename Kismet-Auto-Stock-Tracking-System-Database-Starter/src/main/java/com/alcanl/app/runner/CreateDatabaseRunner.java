package com.alcanl.app.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateDatabaseRunner implements ApplicationRunner {

    @Value("${kismet.auto.stock.tracking.system.database.starter.query}")
    private String databaseExistQuery;

    @Value("${kismet.auto.stock.tracking.system.database.create.query}")
    private String databaseCreateQuery;

    @Value("${kismet.auto.stock.tracking.system.database.starter.process.create.success}")
    private String processSuccessMessage;

    @Value("${kismet.auto.stock.tracking.system.database.starter.process.error}")
    private String processErrorMessage;

    @Value("${kismet.auto.stock.tracking.system.database.starter.process.already.success}")
    private String processAlreadySuccessMessage;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args)
    {
        try {
            var databaseExists = jdbcTemplate.queryForObject(databaseExistQuery, Integer.class);
            if (databaseExists == null || databaseExists != 1) {
                jdbcTemplate.execute(databaseCreateQuery);
                log.info(processSuccessMessage);
            } else
                log.info(processAlreadySuccessMessage);

        } catch (Throwable ex) {
            log.error("Error occurred {} : {}", processErrorMessage, ex.getMessage());
        }
    }
}
