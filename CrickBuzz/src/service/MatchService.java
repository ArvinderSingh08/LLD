package service;

import enums.MatchStatus;
import models.Ball;
import models.Innings;
import models.Match;

import java.util.HashMap;
import java.util.Map;

public class MatchService {
    Map<String, Match> matches = new HashMap<>();

    public void startMatch(Match match) {
        match.start();
        matches.put(match.matchId, match);
    }

    public void addBall(String matchId, Ball ball) {
        Match match = matches.get(matchId);
        if (match == null || match.status != MatchStatus.IN_PROGRESS) {
            throw new RuntimeException("Invalid match");
        }

        Innings innings = match.getCurrentInnings();
        innings.addBall(ball);
        match.notifyObservers(innings);

        if (match.isCompleted()) {
            match.status = MatchStatus.COMPLETED;
        }
    }
}
