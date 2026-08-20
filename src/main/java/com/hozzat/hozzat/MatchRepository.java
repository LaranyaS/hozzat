package com.hozzat.hozzat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // Find all matches involving a particular team
    List<Match> findByTeam1OrTeam2(String team1, String team2);

    // Find matches played between two particular teams
    @Query("""
        SELECT m FROM Match m
        WHERE (m.team1 = :team1 AND m.team2 = :team2)
           OR (m.team1 = :team2 AND m.team2 = :team1)
    """)
    List<Match> findHeadToHead(
            @Param("team1") String team1,
            @Param("team2") String team2
    );

    // Find a team's matches, newest first
    @Query("""
        SELECT m FROM Match m
        WHERE m.team1 = :team OR m.team2 = :team
        ORDER BY m.date DESC
    """)
    List<Match> findMatchesByTeamOrderByDateDesc(
            @Param("team") String team
    );
    @Query("""
    SELECT m FROM Match m
    WHERE (m.team1 = :team OR m.team2 = :team)
      AND m.venue = :venue
    ORDER BY m.date DESC
""")
    List<Match> findMatchesByTeamAndVenue(
            @Param("team") String team,
            @Param("venue") String venue
    );
    @Query("SELECT DISTINCT m.team1 FROM Match m ORDER BY m.team1")
    List<String> findDistinctTeam1s();

    @Query("SELECT DISTINCT m.team2 FROM Match m ORDER BY m.team2")
    List<String> findDistinctTeam2s();

    @Query("SELECT DISTINCT m.venue FROM Match m ORDER BY m.venue")
    List<String> findDistinctVenues();
}