package observer;

import models.Innings;
import models.Match;

public class ScoreboardObserver implements MatchObserver {
    @Override
    public void onScoreUpdate(Match match, Innings innings) {
        System.out.println(
                "LIVE SCORE [" + match.matchId + "]: " +
                        innings.totalRuns + "/" + innings.totalWickets +
                        " (" + innings.getOvers() + " overs)"
        );
    }
}

