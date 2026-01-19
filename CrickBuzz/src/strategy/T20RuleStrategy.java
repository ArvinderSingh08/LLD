package strategy;

import models.Innings;

public class T20RuleStrategy implements MatchRuleStrategy {

    public int maxOvers() {
        return 20;
    }

    public boolean isInningsComplete(Innings innings) {
        return innings.totalWickets == 10 ||
                (innings.overs.size() == 20 && innings.currentOver.isCompleted());
    }
}
