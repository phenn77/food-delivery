package com.melalie.fooddelivery.configuration;

import com.melalie.fooddelivery.service.InsertService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DataLoader implements ApplicationRunner {

    private InsertService insertService;

    public DataLoader(InsertService insertService) {
        this.insertService = insertService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        insertService.execute();
    }
}
