package observer;

import models.Innings;
import models.Match;

public interface MatchObserver {
    void onScoreUpdate(Match match, Innings innings);
}

