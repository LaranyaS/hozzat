package com.hozzat.hozzat;

public class MlPredictionRequest {

    private double team1_recent_win_rate;
    private double team2_recent_win_rate;

    private int team1_recent_matches;
    private int team2_recent_matches;

    private double team1_h2h_win_rate;
    private double team2_h2h_win_rate;

    private int h2h_matches;

    private double team1_venue_win_rate;
    private double team2_venue_win_rate;

    private int team1_venue_matches;
    private int team2_venue_matches;

    public MlPredictionRequest(
            double team1_recent_win_rate,
            double team2_recent_win_rate,
            int team1_recent_matches,
            int team2_recent_matches,
            double team1_h2h_win_rate,
            double team2_h2h_win_rate,
            int h2h_matches,
            double team1_venue_win_rate,
            double team2_venue_win_rate,
            int team1_venue_matches,
            int team2_venue_matches) {

        this.team1_recent_win_rate = team1_recent_win_rate;
        this.team2_recent_win_rate = team2_recent_win_rate;
        this.team1_recent_matches = team1_recent_matches;
        this.team2_recent_matches = team2_recent_matches;
        this.team1_h2h_win_rate = team1_h2h_win_rate;
        this.team2_h2h_win_rate = team2_h2h_win_rate;
        this.h2h_matches = h2h_matches;
        this.team1_venue_win_rate = team1_venue_win_rate;
        this.team2_venue_win_rate = team2_venue_win_rate;
        this.team1_venue_matches = team1_venue_matches;
        this.team2_venue_matches = team2_venue_matches;
    }

    public double getTeam1_recent_win_rate() {
        return team1_recent_win_rate;
    }

    public double getTeam2_recent_win_rate() {
        return team2_recent_win_rate;
    }

    public int getTeam1_recent_matches() {
        return team1_recent_matches;
    }

    public int getTeam2_recent_matches() {
        return team2_recent_matches;
    }

    public double getTeam1_h2h_win_rate() {
        return team1_h2h_win_rate;
    }

    public double getTeam2_h2h_win_rate() {
        return team2_h2h_win_rate;
    }

    public int getH2h_matches() {
        return h2h_matches;
    }

    public double getTeam1_venue_win_rate() {
        return team1_venue_win_rate;
    }

    public double getTeam2_venue_win_rate() {
        return team2_venue_win_rate;
    }

    public int getTeam1_venue_matches() {
        return team1_venue_matches;
    }

    public int getTeam2_venue_matches() {
        return team2_venue_matches;
    }
}