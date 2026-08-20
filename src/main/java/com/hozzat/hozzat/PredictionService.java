package com.hozzat.hozzat;

import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private final MatchService matchService;

    public PredictionService(MatchService matchService) {
        this.matchService = matchService;
    }

    public PredictionResult predict(
            String team1,
            String team2,
            String venue) {

        HeadToHeadStats headToHead =
                matchService.getHeadToHeadStats(team1, team2);

        TeamFormStats team1Form =
                matchService.getRecentForm(team1);

        TeamFormStats team2Form =
                matchService.getRecentForm(team2);

        VenueStats team1Venue =
                matchService.getVenueStats(team1, venue);

        VenueStats team2Venue =
                matchService.getVenueStats(team2, venue);

        double team1Score =
                0.35 * headToHead.getTeam1WinPercentage()
                        + 0.40 * team1Form.getRecentWinPercentage()
                        + 0.25 * team1Venue.getWinPercentage();

        double team2Score =
                0.35 * headToHead.getTeam2WinPercentage()
                        + 0.40 * team2Form.getRecentWinPercentage()
                        + 0.25 * team2Venue.getWinPercentage();

        String predictedWinner;

        if (team1Score > team2Score) {
            predictedWinner = team1;
        } else if (team2Score > team1Score) {
            predictedWinner = team2;
        } else {
            predictedWinner = "Too close to call";
        }

        return new PredictionResult(
                team1,
                team2,
                venue,
                team1Score,
                team2Score,
                predictedWinner
        );
    }
}