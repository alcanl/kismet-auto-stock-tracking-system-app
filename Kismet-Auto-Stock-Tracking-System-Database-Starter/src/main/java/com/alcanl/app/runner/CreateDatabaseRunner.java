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
    private String createQuery;

    @Value("${kismet.auto.stock.tracking.system.database.starter.process.finished}")
    private String processFinishedMessage;

    @Value("${kismet.auto.stock.tracking.system.database.starter.process.error}")
    private String processErrorMessage;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args)
    {
        try {
            jdbcTemplate.execute(createQuery);
            System.out.println(processFinishedMessage);
        } catch (Throwable ex) {
            log.error("Error occurred {} : {}", processErrorMessage, ex.getMessage());
        }
    }
}
