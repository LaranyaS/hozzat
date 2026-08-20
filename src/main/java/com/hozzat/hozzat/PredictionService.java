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
    // INPUT VALIDATION
    // ========================================================

    private void validatePredictionInput(
            String team1,
            String team2,
            String venue) {

        // Check that team 1 was provided
        if (team1 == null || team1.isBlank()) {
            throw new IllegalArgumentException(
                    "team1 is required."
            );
        }

        // Check that team 2 was provided
        if (team2 == null || team2.isBlank()) {
            throw new IllegalArgumentException(
                    "team2 is required."
            );
        }

        // A team cannot play against itself
        if (team1.equalsIgnoreCase(team2)) {
            throw new IllegalArgumentException(
                    "team1 and team2 must be different teams."
            );
        }

        // Check that a venue was provided
        if (venue == null || venue.isBlank()) {
            throw new IllegalArgumentException(
                    "venue is required."
            );
        }

        // Check that team 1 actually exists in our dataset
        if (!matchService.isKnownTeam(team1)) {
            throw new IllegalArgumentException(
                    "Unknown team: " + team1
            );
        }

        // Check that team 2 actually exists in our dataset
        if (!matchService.isKnownTeam(team2)) {
            throw new IllegalArgumentException(
                    "Unknown team: " + team2
            );
        }

        // Check that the venue actually exists in our dataset
        if (!matchService.isKnownVenue(venue)) {
            throw new IllegalArgumentException(
                    "Unknown venue: " + venue
            );
        }
    }


    // ========================================================
    // RULE-BASED PREDICTION
    // ========================================================

    public PredictionResult predict(
            String team1,
            String team2,
            String venue) {

        validatePredictionInput(team1, team2, venue);


        // ----------------------------------------------------
        // Get historical statistics
        // ----------------------------------------------------

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


        // ----------------------------------------------------
        // Calculate rule-based scores
        //
        // 35% head-to-head
        // 40% recent form
        // 25% venue performance
        // ----------------------------------------------------

        double team1Score =
                0.35 * headToHead.getTeam1WinPercentage()
                        + 0.40 * team1Form.getRecentWinPercentage()
                        + 0.25 * team1Venue.getWinPercentage();

        double team2Score =
                0.35 * headToHead.getTeam2WinPercentage()
                        + 0.40 * team2Form.getRecentWinPercentage()
                        + 0.25 * team2Venue.getWinPercentage();


        // ----------------------------------------------------
        // Determine predicted winner
        // ----------------------------------------------------

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

        validatePredictionInput(team1, team2, venue);


        // ----------------------------------------------------
        // Get historical statistics from PostgreSQL
        // ----------------------------------------------------

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


        // ----------------------------------------------------
        // Build the 11 features expected by the ML model
        //
        // Python uses win rates between 0 and 1.
        // Java statistics use percentages between 0 and 100.
        //
        // Example:
        // Java  = 66.67
        // Python = 0.6667
        //
        // Therefore percentage values are divided by 100.
        // ----------------------------------------------------

        MlPredictionRequest request =
                new MlPredictionRequest(

                        // Recent form
                        team1Form.getRecentWinPercentage() / 100.0,
                        team2Form.getRecentWinPercentage() / 100.0,

                        team1Form.getMatchesConsidered(),
                        team2Form.getMatchesConsidered(),

                        // Head-to-head
                        headToHead.getTeam1WinPercentage() / 100.0,
                        headToHead.getTeam2WinPercentage() / 100.0,

                        headToHead.getTotalMatches(),

                        // Venue performance
                        team1Venue.getWinPercentage() / 100.0,
                        team2Venue.getWinPercentage() / 100.0,

                        team1Venue.getMatchesPlayed(),
                        team2Venue.getMatchesPlayed()
                );


        // ----------------------------------------------------
        // Send features to the Python Flask ML service
        // ----------------------------------------------------

        MlPredictionResponse response =
                mlPredictionClient.predict(request);


        // ----------------------------------------------------
        // Convert ML class into team name
        //
        // 1 = team1 wins
        // 0 = team2 wins
        // ----------------------------------------------------

        String predictedWinner =
                response.getPrediction() == 1
                        ? team1
                        : team2;


        // ----------------------------------------------------
        // Return prediction to API
        // ----------------------------------------------------

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