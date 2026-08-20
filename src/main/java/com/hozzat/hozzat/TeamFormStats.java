package com.hozzat.hozzat;

public class TeamFormStats {

    private String team;
    private int matchesConsidered;
    private int wins;
    private int losses;
    private int noResults;
    private double recentWinPercentage;

    public TeamFormStats(
            String team,
            int matchesConsidered,
            int wins,
            int losses,
            int noResults,
            double recentWinPercentage) {

        this.team = team;
        this.matchesConsidered = matchesConsidered;
        this.wins = wins;
        this.losses = losses;
        this.noResults = noResults;
        this.recentWinPercentage = recentWinPercentage;
    }

    public String getTeam() {
        return team;
    }

    public int getMatchesConsidered() {
        return matchesConsidered;
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

    public double getRecentWinPercentage() {
        return recentWinPercentage;
    }
}
