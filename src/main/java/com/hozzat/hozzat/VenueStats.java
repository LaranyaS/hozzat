package com.hozzat.hozzat;

public class VenueStats {

    private String team;
    private String venue;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int noResults;
    private double winPercentage;

    public VenueStats(
            String team,
            String venue,
            int matchesPlayed,
            int wins,
            int losses,
            int noResults,
            double winPercentage) {

        this.team = team;
        this.venue = venue;
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.losses = losses;
        this.noResults = noResults;
        this.winPercentage = winPercentage;
    }

    public String getTeam() {
        return team;
    }

    public String getVenue() {
        return venue;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getNoResults() {
        return noResults;
    }

    public double getWinPercentage() {
        return winPercentage;
    }
}