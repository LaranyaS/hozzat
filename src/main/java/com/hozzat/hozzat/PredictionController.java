package com.hozzat.hozzat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public PredictionResult predict(
            @RequestParam String team1,
            @RequestParam String team2,
            @RequestParam String venue) {

        return predictionService.predict(team1, team2, venue);
    }
}