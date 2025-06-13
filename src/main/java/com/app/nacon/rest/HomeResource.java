package com.app.nacon.rest;

import com.app.nacon.model.TrackingResponse;
import com.app.nacon.service.TrackingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


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
    public Mono<TrackingResponse> getEventDateTime(@PathVariable String trackingNumber) {
        return trackingService.getETA(trackingNumber);
    }
}
