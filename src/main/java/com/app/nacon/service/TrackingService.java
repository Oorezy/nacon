package com.app.nacon.service;

import com.app.nacon.model.TrackingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
//@RequiredArgsConstructor
public class TrackingService {

    private final WebClient webClient;

    public TrackingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://shipsgo.com")
                .build();
    }

    @Value("${shipsGo.authCode}")
    String authCode;

    public String getETA(String trackingId) {

        Mono<TrackingResponse> response = callApi(trackingId);
        return response
                .map(trackingResponse -> {
                    String eta = trackingResponse.getEta();
                    String firstEta = trackingResponse.getFirstEta();
                    return (eta == null || eta.isEmpty()) ? firstEta : eta;
                })
                .block();
    }

//    @Async
    public Mono<String> postContainerInfo(String blReference) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("authCode", authCode);
        formData.add("shippingLine", "ONE LINE");
        formData.add("blContainersRef", "ONEYSHAF89995800");
        return webClient.post()
                .uri("/api/v1.2/ContainerService/PostCustomContainerFormWithBl")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(error -> {
                    // Return the exception message as the response
                    return Mono.just("Error occurred: " + error.getMessage());
                });

    }

    private Mono<TrackingResponse> callApi(String trackingId) {

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


    /*public Mono<String> getTrackingInfo(String trackingNumber) {
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
    }*/


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
