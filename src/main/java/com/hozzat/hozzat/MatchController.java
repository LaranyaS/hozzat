package com.hozzat.hozzat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }
    @GetMapping("/team/{team}")
    public List<Match> getMatchesByTeam(@PathVariable String team) {
        return matchService.getMatchesByTeam(team);
    }
    @GetMapping("/head-to-head")
    public List<Match> getHeadToHead(
            @RequestParam String team1,
            @RequestParam String team2) {

        return matchService.getHeadToHead(team1, team2);
    }
    @GetMapping("/head-to-head/stats")
    public HeadToHeadStats getHeadToHeadStats(
            @RequestParam String team1,
            @RequestParam String team2) {

        return matchService.getHeadToHeadStats(team1, team2);
    }
    @GetMapping("/team/{team}/form")
    public TeamFormStats getRecentForm(@PathVariable String team) {
        return matchService.getRecentForm(team);
    }
    @GetMapping("/team/{team}/venue")
    public VenueStats getVenueStats(
            @PathVariable String team,
            @RequestParam String venue) {

        return matchService.getVenueStats(team, venue);
    }
}