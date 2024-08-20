package com.alcanl.app.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
@Slf4j
public class ParentProcessMessengerRunner implements ApplicationRunner {

    @Value("${kismet.auto.stock.tracking.system.app.start.message}")
    public String processStartedSuccessfullyMessage;

    @Override
    public void run(ApplicationArguments args)
    {
        System.out.println(processStartedSuccessfullyMessage);
    }
}
