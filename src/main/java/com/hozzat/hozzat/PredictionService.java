package com.hozzat.hozzat;

import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private final MatchService matchService;
    private final MlPredictionClient mlPredictionClient;

    public PredictionService(
            MatchService matchService,
            MlPredictionClient mlPredictionClient) {

        this.matchService = matchService;
        this.mlPredictionClient = mlPredictionClient;
    }

    // ========================================================
    // RULE-BASED PREDICTION
    // ========================================================

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


    // ========================================================
    // MACHINE LEARNING PREDICTION
    // ========================================================

    public MlMatchPredictionResult predictWithMl(
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

        // Python was trained using rates from 0 to 1,
        // while Java stores percentages from 0 to 100.
        MlPredictionRequest request =
                new MlPredictionRequest(

                        team1Form.getRecentWinPercentage() / 100.0,
                        team2Form.getRecentWinPercentage() / 100.0,

                        team1Form.getMatchesConsidered(),
                        team2Form.getMatchesConsidered(),

                        headToHead.getTeam1WinPercentage() / 100.0,
                        headToHead.getTeam2WinPercentage() / 100.0,

                        headToHead.getTotalMatches(),

                        team1Venue.getWinPercentage() / 100.0,
                        team2Venue.getWinPercentage() / 100.0,

                        team1Venue.getMatchesPlayed(),
                        team2Venue.getMatchesPlayed()
                );

        MlPredictionResponse response =
                mlPredictionClient.predict(request);

        String predictedWinner =
                response.getPrediction() == 1
                        ? team1
                        : team2;

        return new MlMatchPredictionResult(
                team1,
                team2,
                venue,
                predictedWinner,
                response.getTeam1WinProbability(),
                response.getTeam2WinProbability()
        );
    }
}