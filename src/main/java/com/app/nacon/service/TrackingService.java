package com.app.nacon.service;

import com.app.nacon.model.TrackingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final WebClient.Builder webClientBuilder;

    public Mono<TrackingResponse> getETA(String trackingId) {

        WebClient webClient = webClientBuilder.baseUrl("https://shipsgo.com")
                .build();
        String authCode = "6befb9616038faf26ee7fc4f375cb13d";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1.2/ContainerService/GetContainerInfo/")
                        .queryParam("authCode", authCode)
                        .queryParam("requestId", trackingId)
                        .build())
                .retrieve()
                .bodyToFlux(TrackingResponse.class) // because response is a JSON array
                .next() // take the first object from the list
                .onErrorResume(WebClientResponseException.class, e -> {
                    // Handle specific HTTP errors here if needed
                    System.err.println("WebClient error: " + e.getMessage());
                    return Mono.empty(); // or return a default TrackingResponse
                });
    }


    public Mono<String> getTrackingInfo(String trackingNumber) {
        // Create a WebClient instance
        WebClient webClient = webClientBuilder.baseUrl("https://apis.cma-cgm.net/operation/trackandtrace/v1")
                .defaultHeader("KeyId","NvluGjAGLMkA78w1HB8OpmsYNFLtxyTQ")
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events/{trackingNumber}")
                        .queryParam("limit", 1)
                        .build(trackingNumber))
                .retrieve()
                .bodyToMono(String.class)  // Convert response body to String (or an object)
                .onErrorResume(WebClientResponseException.class, e -> Mono.just("Error: " + e.getMessage()));
    }


                /*.bodyToMono(TrackingResponse.class)  // Map the response to TrackingEventResponse
                .flatMap(response -> {
                    // Extract the eventDateTime from the first event
                    if (response.getEvents() != null && !response.getEvents().isEmpty()) {
                        return Mono.just(response.getEvents().get(0).getEventDateTime());
                    } else {
                        return Mono.just("No events found");
                    }
                })
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));  // Handle errors
    }*/
}
