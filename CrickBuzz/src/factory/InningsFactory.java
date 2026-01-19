package factory;

import models.Innings;
import models.Team;
import strategy.MatchRuleStrategy;

public class InningsFactory {

    public static Innings createInnings(
            Team batting,
            Team bowling,
            MatchRuleStrategy strategy) {

        return new Innings(batting, bowling, strategy);
    }
}
