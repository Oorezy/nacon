package com.app.nacon.rest;

import com.app.nacon.service.TrackingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
public class HomeResource {
    private final TrackingService trackingService;

    public HomeResource(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/")
    public String index() {
        return "\"Hello World!\"";
    }

    @GetMapping("/tracking/{trackingNumber}")
    public String getEventDateTime(@PathVariable String trackingNumber) {
        return trackingService.getETA(trackingNumber);
    }

    @PutMapping("/tracking/{trackingNumber}")
    public Mono<String> getDateTime(@PathVariable String trackingNumber) {
        return trackingService.postContainerInfo(trackingNumber);
    }

}
