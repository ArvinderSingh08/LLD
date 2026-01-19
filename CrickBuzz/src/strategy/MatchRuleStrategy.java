package strategy;

import models.Innings;

public interface MatchRuleStrategy {
    int maxOvers();
    boolean isInningsComplete(Innings innings);
}
