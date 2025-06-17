package com.app.nacon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingResponse {

    @JsonProperty("ArrivalDate")
    private ArrivalDate arrival;

    @JsonProperty("Vessel")
    private String vessel;

    @JsonProperty("Status")
    private String status;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ArrivalDate {

        @JsonProperty("Date")
        private String eta;
    }

}
