package com.app.nacon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingResponse {

    @JsonProperty("ETA")
    private String eta;

    @JsonProperty("FirstETA")
    private String firstEta;

}
