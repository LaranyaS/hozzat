package com.hozzat.hozzat;

public class PredictionResult {

    private String team1;
    private String team2;
    private String venue;

    private double team1Score;
    private double team2Score;

    private String predictedWinner;

    public PredictionResult(
            String team1,
            String team2,
            String venue,
            double team1Score,
            double team2Score,
            String predictedWinner) {

        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.predictedWinner = predictedWinner;
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

    public double getTeam1Score() {
        return team1Score;
    }

    public double getTeam2Score() {
        return team2Score;
    }

    public String getPredictedWinner() {
        return predictedWinner;
    }
}