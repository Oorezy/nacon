package com.app.nacon;

import com.app.nacon.service.ScrapingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
public class NaconApplication {

    ScrapingService scrapingService;

    public static void main(final String[] args) throws IOException {
//        ScrapingService scrapingService = new ScrapingService();

        SpringApplication.run(NaconApplication.class, args);
//        scrapingService.scrape2();
    }

}
