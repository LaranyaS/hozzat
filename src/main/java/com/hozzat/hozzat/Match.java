package com.hozzat.hozzat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    private Long id;

    private String season;

    private String city;

    private LocalDate date;

    private String team1;

    private String team2;

    private String winner;

    @Column(name = "toss_winner")
    private String tossWinner;

    @Column(name = "toss_decision")
    private String tossDecision;

    private String result;

    @Column(name = "dl_applied")
    private Integer dlApplied;

    @Column(name = "win_by_runs")
    private Integer winByRuns;

    @Column(name = "win_by_wickets")
    private Integer winByWickets;

    @Column(name = "player_of_match")
    private String playerOfMatch;

    private String venue;

    private String umpire1;

    private String umpire2;

    private String umpire3;


    // GETTERS

    public Long getId() {
        return id;
    }

    public String getSeason() {
        return season;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getWinner() {
        return winner;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public String getTossDecision() {
        return tossDecision;
    }

    public String getResult() {
        return result;
    }

    public Integer getDlApplied() {
        return dlApplied;
    }

    public Integer getWinByRuns() {
        return winByRuns;
    }

    public Integer getWinByWickets() {
        return winByWickets;
    }

    public String getPlayerOfMatch() {
        return playerOfMatch;
    }

    public String getVenue() {
        return venue;
    }

    public String getUmpire1() {
        return umpire1;
    }

    public String getUmpire2() {
        return umpire2;
    }

    public String getUmpire3() {
        return umpire3;
    }
}