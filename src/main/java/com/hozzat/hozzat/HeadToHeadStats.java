package com.hozzat.hozzat;

public class HeadToHeadStats {

    private String team1;
    private String team2;
    private int totalMatches;
    private int team1Wins;
    private int team2Wins;
    private int noResults;
    private double team1WinPercentage;
    private double team2WinPercentage;

    public HeadToHeadStats(
            String team1,
            String team2,
            int totalMatches,
            int team1Wins,
            int team2Wins,
            int noResults,
            double team1WinPercentage,
            double team2WinPercentage) {

        this.team1 = team1;
        this.team2 = team2;
        this.totalMatches = totalMatches;
        this.team1Wins = team1Wins;
        this.team2Wins = team2Wins;
        this.noResults = noResults;
        this.team1WinPercentage = team1WinPercentage;
        this.team2WinPercentage = team2WinPercentage;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getTeam1Wins() {
        return team1Wins;
    }

    public int getTeam2Wins() {
        return team2Wins;
    }

    public int getNoResults() {
        return noResults;
    }

    public double getTeam1WinPercentage() {
        return team1WinPercentage;
    }

    public double getTeam2WinPercentage() {
        return team2WinPercentage;
    }
}