package com.hozzat.hozzat;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MlPredictionClient {

    private final RestClient restClient;

    public MlPredictionClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:5000")
                .build();
    }

    public MlPredictionResponse predict(MlPredictionRequest request) {

        return restClient.post()
                .uri("/predict")
                .body(request)
                .retrieve()
                .body(MlPredictionResponse.class);
    }
}