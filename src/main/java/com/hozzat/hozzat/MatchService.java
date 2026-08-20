package com.hozzat.hozzat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getMatchesByTeam(String team) {
        return matchRepository.findByTeam1OrTeam2(team, team);
    }

    public List<Match> getHeadToHead(String team1, String team2) {
        return matchRepository.findHeadToHead(team1, team2);
    }

    public HeadToHeadStats getHeadToHeadStats(String team1, String team2) {

        // Get all historical matches between these two teams
        List<Match> matches = matchRepository.findHeadToHead(team1, team2);

        int totalMatches = matches.size();
        int team1Wins = 0;
        int team2Wins = 0;
        int noResults = 0;

        // Look at the winner of every match
        for (Match match : matches) {

            if (match.getWinner() == null) {
                noResults++;
            } else if (match.getWinner().equals(team1)) {
                team1Wins++;
            } else if (match.getWinner().equals(team2)) {
                team2Wins++;
            }
        }

        // Avoid division by zero if the teams have never played
        double team1WinPercentage = totalMatches == 0
                ? 0
                : (team1Wins * 100.0) / totalMatches;

        double team2WinPercentage = totalMatches == 0
                ? 0
                : (team2Wins * 100.0) / totalMatches;

        // Package our calculated result into the DTO
        return new HeadToHeadStats(
                team1,
                team2,
                totalMatches,
                team1Wins,
                team2Wins,
                noResults,
                team1WinPercentage,
                team2WinPercentage
        );
    }
    public TeamFormStats getRecentForm(String team) {

        // Get this team's matches ordered newest → oldest
        List<Match> allMatches =
                matchRepository.findMatchesByTeamOrderByDateDesc(team);

        // Only look at the most recent 10 matches
        int numberOfMatches = Math.min(10, allMatches.size());

        List<Match> recentMatches =
                allMatches.subList(0, numberOfMatches);

        int wins = 0;
        int losses = 0;
        int noResults = 0;

        for (Match match : recentMatches) {

            if (match.getWinner() == null) {
                noResults++;
            } else if (match.getWinner().equals(team)) {
                wins++;
            } else {
                losses++;
            }
        }

        double recentWinPercentage = numberOfMatches == 0
                ? 0
                : (wins * 100.0) / numberOfMatches;

        return new TeamFormStats(
                team,
                numberOfMatches,
                wins,
                losses,
                noResults,
                recentWinPercentage
        );
    }
    public VenueStats getVenueStats(String team, String venue) {

        List<Match> matches =
                matchRepository.findMatchesByTeamAndVenue(team, venue);

        int matchesPlayed = matches.size();
        int wins = 0;
        int losses = 0;
        int noResults = 0;

        for (Match match : matches) {

            if (match.getWinner() == null) {
                noResults++;
            } else if (match.getWinner().equals(team)) {
                wins++;
            } else {
                losses++;
            }
        }

        double winPercentage = matchesPlayed == 0
                ? 0
                : (wins * 100.0) / matchesPlayed;

        return new VenueStats(
                team,
                venue,
                matchesPlayed,
                wins,
                losses,
                noResults,
                winPercentage
        );
    }
}