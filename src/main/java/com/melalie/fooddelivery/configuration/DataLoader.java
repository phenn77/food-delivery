package com.melalie.fooddelivery.configuration;

import com.melalie.fooddelivery.service.ResetService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ResetService resetService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        resetService.execute();
    }
}
