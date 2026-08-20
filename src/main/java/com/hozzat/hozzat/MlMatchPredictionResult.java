package com.hozzat.hozzat;

public class MlMatchPredictionResult {

    private String team1;
    private String team2;
    private String venue;
    private String predictedWinner;
    private double team1WinProbability;
    private double team2WinProbability;

    public MlMatchPredictionResult(
            String team1,
            String team2,
            String venue,
            String predictedWinner,
            double team1WinProbability,
            double team2WinProbability) {

        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.predictedWinner = predictedWinner;
        this.team1WinProbability = team1WinProbability;
        this.team2WinProbability = team2WinProbability;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getVenue() {
        return venue;
    }

    public String getPredictedWinner() {
        return predictedWinner;
    }

    public double getTeam1WinProbability() {
        return team1WinProbability;
    }

    public double getTeam2WinProbability() {
        return team2WinProbability;
    }
}