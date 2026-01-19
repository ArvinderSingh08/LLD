package models;

import enums.MatchStatus;
import factory.InningsFactory;
import observer.MatchObserver;
import strategy.MatchRuleStrategy;

import java.util.ArrayList;
import java.util.List;

public class Match {
    public String matchId;
    Team teamA;
    Team teamB;

    public MatchStatus status = MatchStatus.NOT_STARTED;
    MatchRuleStrategy ruleStrategy;

    Innings firstInnings;
    Innings secondInnings;

    List<MatchObserver> observers = new ArrayList<>();

    public Match(String matchId, Team teamA, Team teamB,
          MatchRuleStrategy ruleStrategy) {
        this.matchId = matchId;
        this.teamA = teamA;
        this.teamB = teamB;
        this.ruleStrategy = ruleStrategy;
    }

    public void registerObserver(MatchObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Innings innings) {
        for (MatchObserver o : observers) {
            o.onScoreUpdate(this, innings);
        }
    }

    public void start() {
        firstInnings = InningsFactory.createInnings(
                teamA, teamB, ruleStrategy
        );
        status = MatchStatus.IN_PROGRESS;
    }

    public Innings getCurrentInnings() {
        if (!firstInnings.isCompleted()) {
            return firstInnings;
        }
        if (secondInnings == null) {
            secondInnings = InningsFactory.createInnings(
                    teamB, teamA, ruleStrategy
            );
        }
        return secondInnings;
    }

    public boolean isCompleted() {
        return secondInnings != null && secondInnings.isCompleted();
    }
}

