package com.app.nacon;

import com.app.nacon.service.ScrapingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;


@SpringBootApplication
@EnableScheduling
public class NaconApplication {

    ScrapingService scrapingService;

    public static void main(final String[] args) {

        SpringApplication.run(NaconApplication.class, args);
    }

}
