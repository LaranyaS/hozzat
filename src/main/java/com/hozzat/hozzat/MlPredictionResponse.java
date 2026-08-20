package com.hozzat.hozzat;

public class MlPredictionResponse {

    private int prediction;
    private double team1WinProbability;
    private double team2WinProbability;

    public int getPrediction() {
        return prediction;
    }

    public void setPrediction(int prediction) {
        this.prediction = prediction;
    }

    public double getTeam1WinProbability() {
        return team1WinProbability;
    }

    public void setTeam1WinProbability(double team1WinProbability) {
        this.team1WinProbability = team1WinProbability;
    }

    public double getTeam2WinProbability() {
        return team2WinProbability;
    }

    public void setTeam2WinProbability(double team2WinProbability) {
        this.team2WinProbability = team2WinProbability;
    }
}